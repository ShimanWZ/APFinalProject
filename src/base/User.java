package base;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class User {
	private String username, password, emailAddress, passwordQuestion, passwordAnswer;
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int tictactoeCompWins, tictactoeCompLosses;
	
	//----------------------------- constructor --------------------------//
	public User(String username, String password, String emailAddress) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
	}
	
	
	//----------------------------- getters and setters ------------------------//
	public void setServerProperties(Socket socket, OutputStream outputStream, InputStream inputStream) {
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
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
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getPasswordQuestion() {
		return passwordQuestion;
	}
	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}
	public String getPasswordAnswer() {
		return passwordAnswer;
	}
	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}
}
