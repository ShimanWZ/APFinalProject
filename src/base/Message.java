package base;

import java.io.Serializable;

import application.Main;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sender, reciever, text;
	private boolean read = false;
	
	public Message(String sender, String reciever, String text) {
		this.sender = sender;
		this.reciever = reciever;
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public String getReciever() {
		return reciever;
	}
	public String getText() {
		return text;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean b) {
		this.read = b;
	}
	public String toString() {
		User curUser = Main.getCurUser();
		if (this.sender.equalsIgnoreCase(curUser.getUsername())) 
			return  this.reciever + " \n " + "	you : "+ this.text;
		return  this.sender + " \n	 " + this.sender+ " : " + this.text;
	}
}
