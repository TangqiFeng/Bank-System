import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ServerSocket m_ServerSocket = new ServerSocket(2004, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
			cliThread.start();

		}
	}
}

class ClientServiceThread extends Thread {
	Socket clientSocket;
	String message;
	int clientID;
	boolean running = true;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean flag = true; // check whether break login loop
	Person user; // used to load logined user object and modify it
					// (lodgement/Withdrawal)
	String clientName;// after login, reload user's file

	ClientServiceThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server > " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void run() {
		// System.out.println(
		// "Accepted Client : ID - " + clientID + " : Address - " +
		// clientSocket.getInetAddress().getHostName());
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Accepted Client : ID - " + clientID + " : Address - "
					+ clientSocket.getInetAddress().getHostAddress());

			sendMessage("Connection successful");
			System.out.println("server > " + " Client : ID - " + clientID + "  Connection successful");

			askLogin();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ask for whether login
	void askLogin() {
		try {
			message = (String) in.readObject();
			System.out.println("client> " + clientID + "  " + message);
			if (message.equals("login")) {
				login();

			}
			if (message.equals("register")) {
				register();
			}
			if (message.equals("exit")) {
				System.out.println("server> " + clientID + " exit ! ");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// login function
	void login() throws ClassNotFoundException, IOException {
		boolean j = false; // check if the user existed
		String[] str = new File("data//" + File.separator).list();
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		}
		while (flag) { // if login error repeat
			String username = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  username:  " + username);
			String password = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  password:  " + password);
			j = false;
			for (int i = 0; i < str.length; i++) {
				System.out.println(str[i]);
				if (str[i].startsWith(username)) {
					j = true;
				}
			}
			if (!j) {
				sendMessage("nouser");
			} else {
				ObjectInputStream inn = new ObjectInputStream(new FileInputStream("data//" + username + ".dat"));
				clientName = username;
				Person who = (Person) inn.readObject();
				inn.close();
				if (!password.equals(who.getPassword())) {
					sendMessage("nopass");
					j = false;
				} else {
					flag = false;
					operation();
				}

			}

		}
		flag = true;
	}

	// operations
	void operation() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream infile = new ObjectInputStream(new FileInputStream("data//" + clientName + ".dat"));
		user = (Person) infile.readObject();
		infile.close();
		String balance = (user.getBalance()) + "";
		sendMessage(balance);
		sendMessage(balance);
		boolean f = true; // f control the logement/withdrawal loop
		while (f) { // repeat logement/withdrawal operation
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  operation:  " + message);
			if (message.equals("logement")) {
				String v = (String) in.readObject();
				System.out.println("client> [" + clientID + "]  value:  " + v);
				int value = Integer.parseInt(v);
				user.setBalance(user.getBalance() + value);
				user.addOperation("logement", value, user.getBalance());
			}
			if (message.equals("withdrawal")) {
				while(true){
					String v = (String) in.readObject();
					System.out.println("client> [" + clientID + "  value:  " + v);
					int value = Integer.parseInt(v);
					if(user.getBalance() - value < -1000){
						sendMessage("error");
					}else{
						sendMessage("ok");
						user.setBalance(user.getBalance() - value);
						user.addOperation("withdrawal", value, user.getBalance());
						break;
					}
				}
			}
			if (message.equals("showHistory")) {
				ObjectInputStream inn = new ObjectInputStream(new FileInputStream("data//" + clientName + ".dat"));
				Person who = (Person) inn.readObject();
				System.out.println(who.getAddress());
				inn.close();
				ArrayList<Event> rs = who.getEvents();
				int j = rs.size();
				for (int i = 0; i < j; i++) {
					System.out.println((i + 1) + " :  " + rs.get(i).getOperate() + " " + rs.get(i).getValue() + " "
							+ rs.get(i).getBalance());
				}
				System.out.println(j);
				sendMessage(j + "");
				for (int i = 0; i < 10 && i < j; i++) {
					System.out.println((i + 1) + " :  " + rs.get(i).getOperate() + " " + rs.get(i).getValue() + " "
							+ rs.get(i).getBalance());
					sendMessage(rs.get(i).getOperate());
					sendMessage(rs.get(i).getValue() + "");
					sendMessage(rs.get(i).getBalance() + "");
				}
			}
			if (message.equals("changePassword")) {
				String password = (String) in.readObject();
				System.out.println("client> [" + clientID + "]  new password:  " + password);
				user.setPassword(password);
				f = false;
			}
			if (message.equals("logout")) {
				f = false;
			}
			FileOutputStream outstream = new FileOutputStream("data//" + clientName + ".dat");
			ObjectOutputStream out = new ObjectOutputStream(outstream);
			out.writeObject(user); 
			out.close();
		}

		askLogin();
	}

	// register function
	void register() {
		try {
			// store as object
			Person me = new Person();
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  name:  " + message);
			me.setName(message);
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  address:  " + message);
			me.setAddress(message);
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  bank-A/C-number:  " + message);
			me.setBankNumber(message);
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  userName:  " + message);
			me.setUsername(message);
			message = (String) in.readObject();
			System.out.println("client> [" + clientID + "]  password:  " + message);
			me.setPassword(message);
			me.setBalance(0);
			// write to a file( .dat )
			FileOutputStream outstream = new FileOutputStream("data//" + me.getUsername() + ".dat");
			ObjectOutputStream out = new ObjectOutputStream(outstream);
			out.writeObject(me);
			out.close();

			askLogin();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
