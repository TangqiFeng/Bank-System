import java.io.Serializable;

@SuppressWarnings("serial")
public class Event implements Serializable{
	private String operate;
	private int value;
	private int balance;
	
	public Event(String operate, int value, int balance) {
		super();
		this.operate = operate;
		this.value = value;
		this.balance = balance;
	}
	
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	

}
