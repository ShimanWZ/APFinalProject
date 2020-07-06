package application;

import java.io.IOException;
import java.io.ObjectOutputStream;

import base.MessageListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainChatSceneController {
	@FXML private TextField textField;
	@FXML private ListView<String> messages;
	
	{
		Main.getCurUser().addMessageListener(new MessageListener() {
			@Override
			public void onMessage(String user, String message) {
				if (user.equalsIgnoreCase(Main.getContact())) messages.getItems().add(user + " : " + message);
			}
		});
	}
	public void sendMessage() throws IOException {
		System.out.println("sending called...");
		String from = Main.getCurUser().getUsername();
		String to = Main.getContact();
		String message = textField.getText();
		ObjectOutputStream objOut = Main.getObjOut();
		
		
		if (!message.equals("")) {
			String[] command = initializeCommandString(from, to, message);
			objOut.writeObject(command);
			objOut.flush();
			messages.getItems().add("you : " + message);
			textField.setText("");
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
	public void addMessage(String s){
		messages.getItems().add(s);
	}
}
