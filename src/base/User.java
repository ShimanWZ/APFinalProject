package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import application.Main;
import application.MainChatSceneController;
import application.TicTacToeController;
import games.TicTacToe;
import javafx.application.Platform;
import server.Server;

public class User implements Serializable {
	private static final long serialVersionUID = -1470082223508490796L;
	private int tictactoeCompWins = 0, tictactoeCompLosses = 0, tictactoePlayWins = 0, tictactoePlayLosses = 0;
	private String username, password, emailAddress, passwordQuestion, passwordAnswer;
	private ArrayList<String> contacts  = new ArrayList<>();
	private ArrayList<LinkedList<Message>> messages;
	private transient static ArrayList<MessageListener> messageListener = new ArrayList<>();
	private transient static ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
	private transient OpponentMoveListener opponentMoveListener;
	
	
	//----------------------------- constructor --------------------------//
	public User(String username) {
		this.username = username;
		contacts  = new ArrayList<>();
	}
	public User(String username, String password, String emailAddress) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		contacts  = new ArrayList<>();
		messages = new ArrayList<>();
	}
	//--------------------------------------------------------------------------//
	//------------------------- server related methods -------------------------//
	//--------------------------------------------------------------------------//
	
	//------------------------------signIn methods -----------------------------
	public static String signIn(String username, String password) throws IOException, ClassNotFoundException {
		BufferedReader in = Main.getBufferedReader();
		ObjectOutputStream objOI = Main.getObjOut();
		
		//initializing & sending request array....
		String[] login = new String[3];
		
		login[0] = "login";
		login[1] = username;
		login[2] = password;
		
		objOI.writeObject(login);
		objOI.flush();
		
		// recieving server response
		String serverAnswer = in.readLine();
		System.out.println("serv ans: " + serverAnswer);
		String ansHandler = serverAnsHandler(serverAnswer);
		return ansHandler;
	}
	private static String serverAnsHandler(String serverAnswer) throws ClassNotFoundException, IOException {
		if (serverAnswer.equalsIgnoreCase("ok")) {
			return "yes";
		}
		else if (serverAnswer.equalsIgnoreCase("wrongpass")) return "Wrong Password!";
		else return "No such user found!";
	}
	public static void messageReader() {
		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {
				readMessageLoop();
			}
		});
		t.start();		
	}
	private static void readMessageLoop() {
		String[] commandArray;
		ObjectInputStream objectIS = Main.getObjIn();		
		try {
			while ((commandArray = (String[])(objectIS.readObject())) != null) {
				if (commandArray[0].equalsIgnoreCase("recieve")) {
					handleRecievingMessage(commandArray);
				}else if (commandArray[0].equalsIgnoreCase("online")) {
					handleOnline(commandArray);
				}else if (commandArray[0].equalsIgnoreCase("offline")) {
					handleOffline(commandArray);
				}else if (commandArray[0].equalsIgnoreCase("gameProperties")) {
					System.out.println("properties recieved");
					handleMoveProperties(commandArray);
				}
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void handleRecievingMessage(String[] commandArray) {
		System.out.println(commandArray[1] + " : " + commandArray[2]);
		for (MessageListener listener: messageListener) {
			listener.onMessage(commandArray[1], commandArray[2]);
		}
		System.out.println("added");
	}
	private static void handleOffline(String[] commandArray) {
		for (UserStatusListener listener : userStatusListeners) {
			listener.offline(commandArray[1]);
		}
	}
	private static void handleOnline(String[] commandArray) {
		for (UserStatusListener listener : userStatusListeners) {
			listener.online(commandArray[1]);
		}
	}
	private static void handleMoveProperties(String[] commandArray) {
		int i =Integer.parseInt(commandArray[1]);
		int j = Integer.parseInt(commandArray[2]);
		Main.getCurUser().getOpponentMoveListener().onMoveMade(i, j);
		TicTacToe.setLastOponentI(i);
		TicTacToe.setLastOponentJ(j);
		TicTacToeController.propertiesRecieved = true;
		System.out.println("properties recieved : " + commandArray[1] + commandArray[2]);
	}
	//--------------------------------------------------------------------------//
	//----------------------------- getters and setters ------------------------//
	//--------------------------------------------------------------------------//
	//------------personal data getters & setters
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
	//-------------------------------------------------------------------------------//
	//----------------wins & losses getters & setters
	public int getTictactoeCompLosses() {
		return tictactoeCompLosses;
	}
	public void setTictactoeCompLosses(int tictactoeCompLosses) {
		this.tictactoeCompLosses = tictactoeCompLosses;
	}
	public int getTictactoeCompWins() {
		return tictactoeCompWins;
	}
	public void setTictactoeCompWins(int tictactoeCompWins) {
		this.tictactoeCompWins = tictactoeCompWins;
	}
	public int getTictactoePlayWins() {
		return tictactoePlayWins;
	}
	public void setTictactoePlayWins(int tictactoePlayWins) {
		this.tictactoePlayWins = tictactoePlayWins;
	}
	public void setTictactoePlayLosses(int tictactoePlayLosses) {
		this.tictactoePlayLosses = tictactoePlayLosses;
	}
	public int getTictactoePlayLosses() {
		return tictactoePlayLosses;
	}
	public void addMessageListener(MessageListener listener) {
		messageListener.add(listener);
	}
	public void removeMessageListener(MessageListener listener) {
		messageListener.remove(listener);
	}
	public void addUserStatusListener(UserStatusListener listener) {
		userStatusListeners.add(listener);
	}
	public void removeUserStatusListener(UserStatusListener listener) {
		userStatusListeners.remove(listener);
	}
	public ArrayList<String> getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList<String> contacts) {
		this.contacts = contacts;
	}
	public void addContact(String contact) {
		if (!this.contacts.contains(contact)) this.contacts.add(contact);
	}
	public ArrayList<LinkedList<Message>> getMessages() {
		return messages;
	}
	public void addMessage(Message message, String otheUser) {
		boolean isFirstChat = true;
		for (LinkedList<Message> messageList : this.messages) {
			boolean firstCondition = messageList.getFirst().getSender().equals(otheUser);
			boolean secondCondition = messageList.getFirst().getReciever().equals(otheUser);
			
			if(firstCondition || secondCondition) {
				messageList.add(message);
				System.out.println("messageadded!!!");
				isFirstChat = false;
				break;
			}
		}
		if (isFirstChat) {
			LinkedList<Message> first = new LinkedList<Message>();
			first.add(message);
			this.messages.add(first);
		}
	}
	public OpponentMoveListener getOpponentMoveListener() {
		return opponentMoveListener;
	}
	public void setOpponentMoveListener(OpponentMoveListener opponentMoveListener) {
		this.opponentMoveListener = opponentMoveListener;
	}
}