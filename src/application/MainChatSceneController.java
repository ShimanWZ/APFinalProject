package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import base.Message;
import base.MessageListener;
import base.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainChatSceneController {
	@FXML private TextField textField;
	@FXML private ListView<String> messages;
	@FXML private Label username;
	private boolean init = true;
	private String smile = new String(Character.toChars(0x1F642));
	private String laugh = new String(Character.toChars(0x1F602));
	private String wink = new String(Character.toChars(0x1F609));
	private String kiss = new String(Character.toChars(0x1F618));
	private String tongue = new String(Character.toChars(0x1F61C));
	private String neutral = new String(Character.toChars(0x1F610));
	private String sad = new String(Character.toChars(0x1F614));
	private String flushed = new String(Character.toChars(0x1F633));
	private String heart = new String(Character.toChars(0x2764));
	
	
	
	{
		Main.getCurUser().addMessageListener(new MessageListener() {
			@Override
			public void onMessage(String user, String message) {
				if (user.equalsIgnoreCase(Main.getContact())) {
					Platform.runLater(new Runnable() {
			            @Override public void run() {
							messages.getItems().add(user + " : " + message);
			            }
					});
					setMessageOnRead(); //sets all the messages in this chat to true.
				}
			}
		});
	}
	
	
	@FXML private void sendMessage() throws IOException {
		String from = Main.getCurUser().getUsername();
		String to = Main.getContact();
		String message = textField.getText();
		ObjectOutputStream objOut = Main.getObjOut();
		
		
		if (!message.equals("")) {
			
			String[] command = initializeCommandString(from, to, message);
			objOut.writeObject(command);
			objOut.flush();
			
			messages.getItems().add("you : " + message); //adding to listview
			
			Main.getCurUser().addMessage(new Message(from, to, message), to);//adding to user messages
			setMessageOnRead();
			textField.setText("");
		}
	}
	@FXML private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
	@FXML private void initializingMessages() {
		if (init) {
			username.setText(Main.getContact());
			for (LinkedList<Message> messages : Main.getCurUser().getMessages()) {
				
				boolean firstCondition = messages.getFirst().getSender().equals(Main.getContact());
				boolean secondCondition = messages.getFirst().getReciever().equals(Main.getContact());
				
				if (firstCondition || secondCondition) {
					Iterator<Message> iterator = messages.iterator();
					while (iterator.hasNext()) {
						Message curMessage = iterator.next();
						if (curMessage.getSender().equals(Main.getCurUser().getUsername())) {
							this.messages.getItems().add("you : " + curMessage.getText());
						}
						else this.messages.getItems().add(curMessage.getSender() + " : " + curMessage.getText());
						
						curMessage.setRead(true);
					}				
				}				
			}
			init = false;
		}
	}
	private String[] initializeCommandString(String from, String to, String message) {
		String[] command = new String[4];
		command[0] = "send";
		command[1] = from;
		command[2] = to;
		command[3] = message;
		return command;
	}
	private void setMessageOnRead() {
		User user = Main.getCurUser();
		for (LinkedList<Message> messages : user.getMessages()) {
			//sets all the messages from this user to read.
			if (messages.getFirst().getSender().equals(Main.getContact()) || messages.getFirst().getReciever().equals(Main.getContact())) {
				Iterator<Message> iterator = messages.iterator();
				while (iterator.hasNext()) {
					iterator.next().setRead(true);
				}
			}
		}
	}
	
	@FXML private void addSmile(){
		textField.setText(textField.getText() + " " + smile);
	}
	@FXML private void addKiss(){
		textField.setText(textField.getText() + " " + kiss);
	}@FXML private void addLaugh(){
		textField.setText(textField.getText() + " " + laugh);
	}@FXML private void addWink(){
		textField.setText(textField.getText() + " " + wink);
	}@FXML private void addTongue(){
		textField.setText(textField.getText() + " " + tongue);
	}@FXML private void addNeutral(){
		textField.setText(textField.getText() + " " + neutral);
	}@FXML private void addSad(){
		textField.setText(textField.getText() + " " + sad);
	}@FXML private void addFlushed(){
		textField.setText(textField.getText() + " " + flushed);
	}
	@FXML private void addHeart(){
		textField.setText(textField.getText() + " " + heart);
	}
}

