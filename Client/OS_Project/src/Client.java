import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message = "";
	String ipaddress;
	Scanner stdin;

	Client() {
	}

	void run() throws Exception {
		stdin = new Scanner(System.in);
		try {
			// 1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address");
			ipaddress = stdin.next();
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to " + ipaddress + " in port 2004");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			// 3. Communicating with the server
			 
			checkConn();
			
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	//send message
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			//System.out.println("client> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	//check is connected
	void checkConn() throws Exception{
		try {
			message = (String) in.readObject();
			if(message.equals("Connection successful"))
			System.out.println("connect successful !");
			askLogin();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ask for whether login
	void askLogin() throws Exception{
		System.out.println("Hello, please choose: ");
		System.out.println("1  Login ");
		System.out.println("2  Register ");
		System.out.println("9  EXIT ! ");
		int i = stdin.nextInt();
		while(true){ // if input wrong number, try again
			if(i == 1){
				sendMessage("login");
				login();
				break;
			}else if(i == 2){
				sendMessage("register");
				register();
				break;
			}else if(i == 9){
				sendMessage("exit");
				System.out.println("  bye-bye  @.@ ");
				break;
			}else{
				System.out.println("ERROR!!! Please try again !");
				i = stdin.nextInt();
			}
		}
	}
	
	void register() throws Exception{
		System.out.println("Please input name: ");
		String name = stdin.next();
		sendMessage(name);
		System.out.println("Please input address: ");
		String address = stdin.next();
		sendMessage(address);
		System.out.println("Please input Bank A/C Number: ");
		String bankNum = stdin.next();
		sendMessage(bankNum);
		System.out.println("Please input username: ");
		String username = stdin.next();
		sendMessage(username);
		System.out.println("Please input password: ");
		String password = stdin.next();
		sendMessage(password);
		askLogin();
	}
	
	void login() throws Exception{
		System.out.println("Please input username: ");
		String username = stdin.next();
		sendMessage(username);
		System.out.println("Please input password: ");
		String password = stdin.next();
		sendMessage(password);
		try {
			String msg = (String) in.readObject();
			//System.out.println(msg);
			if(msg.equals("nouser")){        //user name is wrong
				System.out.println("ERROR >>> No such user !");
				System.out.println();
				System.out.println("press 1 try again !");
				System.out.println("press 2 back home !");
				int i = stdin.nextInt();
				System.out.println();
				if(i == 1){
					login();
				}
				if(i == 2){
					askLogin();
				}
			}else if(msg.equals("nopass")){     //password is wrong
				System.out.println("ERROR >>> User/Password is wrong !");
				System.out.println();
				System.out.println("press 1 try again !");
				System.out.println("press 2 back home !");
				int i = stdin.nextInt();
				System.out.println();
				if(i == 1){
					login();
				}
				if(i == 2){
					askLogin();
				}
			}else{
				operation();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void operation() throws Exception {
		
		String msg = (String) in.readObject();
		System.out.println();
		System.out.println("==>> Welcome '_' ");
		System.out.println();
		System.out.println("Your account : "+msg);
		boolean flag = true;
		while(flag){
			System.out.println("Please choose: ");
			System.out.println("=> l    Lodgement");
			System.out.println("=> w    Withdrawal");
			System.out.println("=> h    Show History");
			System.out.println("=> c    Change Password");
			System.out.println("=> e    logout");
			String m = stdin.next(); //m is operation
			String v; // v is the value
		
			while(true){
				if(m.equals("l")){
					sendMessage("logement");
					System.out.println("Please input value");
					v = stdin.next();
					sendMessage(v);
					break;
				}else if(m.equals("w")){
					sendMessage("withdrawal");
					while(true){
						System.out.println("Please input value");
						v = stdin.next();
						sendMessage(v);
						String signal = (String) in.readObject();
						if(signal.equals("error")){
							System.out.println("ERROR ==>> Each User has a credit limit of 1000");
							System.out.println("Please try again");
						}
						if(signal.equals("ok")){
							break;
						}
					}
					sendMessage(v);
					break;
				}else if(m.equals("h")){
					sendMessage("showHistory");
					int sum = Integer.parseInt((String) in.readObject());
					System.out.println("HISTORY (maxmum:10) :");
					for(int i=0;i<10&&i<sum;i++){
					String msg1 = (String) in.readObject();
					String msg2 = (String) in.readObject();
					String msg3 = (String) in.readObject();
					System.out.println((i+1) + " :  "+msg1+"   "+msg2+"   "+msg3);
					}
					System.out.println();
					break;
				}else if(m.equals("c")){
					sendMessage("changePassword");
					System.out.println("Please input new password:");
					String password = stdin.next();
					sendMessage(password);
					System.out.println("Your password changed ~ ~");
					askLogin();
					flag = false;
					break;
				}else if(m.equals("e")){
					sendMessage("logout");
					askLogin();
					flag = false;
					break;
				}else{
					System.out.println("ERROR!!! Please try again !");
					m = stdin.next();
				}
			}		
		}
		
		
		
		
	}

	public static void main(String args[]) throws Exception {
		Client client = new Client();
		client.run();
	}
}