package base;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sender, reciever, text;
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
}
