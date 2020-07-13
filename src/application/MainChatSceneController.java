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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainChatSceneController {
	@FXML private TextField textField;
	@FXML private ListView<String> messages;
	private boolean init = true;
	
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
					setMessageOnRead();
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
			messages.getItems().add("you : " + message);
			Main.getCurUser().addMessage(new Message(Main.getCurUser().getUsername(), to, message), to);
			setMessageOnRead();
			textField.setText("");
		}
	}
	@FXML private void initializingMessages() {
		if (init) {
			System.out.println("in intiing");
			for (LinkedList<Message> messages : Main.getCurUser().getMessages()) {
				
				boolean firstCondition = messages.getFirst().getSender().equals(Main.getContact());
				boolean secondCondition = messages.getFirst().getReciever().equals(Main.getContact());
				
				if (firstCondition || secondCondition) {
					System.out.println("one of the conditions matches");
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
			if (messages.getFirst().getSender().equals(Main.getContact()) || messages.getFirst().getReciever().equals(Main.getContact())) {
				Iterator<Message> iterator = messages.iterator();
				while (iterator.hasNext()) {
					iterator.next().setRead(true);
				}
			}
		}
	}
}

