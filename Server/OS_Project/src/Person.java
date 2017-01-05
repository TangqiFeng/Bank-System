import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
class Person implements Serializable{
    private String name;
    private String address;
    private String bankNumber;
    private String username;
    private String password;
    private int balance;
    ArrayList<Event> events = new ArrayList<Event>();
    
    public void addOperation(String opt,int val,int sum){
    	events.add(0,new Event(opt, val, sum));
    }
    
    public void getLog(){
         for(int i=0;;i++){
         	System.out.println(events.get(i).getOperate()+" "+events.get(i).getValue()+" "+events.get(i).getBalance());
         }
    }
    
    public Person() {
		this.balance = 0;
	}
    
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}