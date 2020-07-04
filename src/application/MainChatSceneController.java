package application;

import java.io.IOException;
import java.io.ObjectOutputStream;

import base.MessageListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainChatSceneController {
	@FXML private AnchorPane contacts;
	@FXML private AnchorPane chatView;
	@FXML private ListView<String> list;
	@FXML private TextField textField;
	@FXML private ListView<String> messages;
	private String currentContact;
	
	{
		Main.getCurUser().addMessageListener(new MessageListener() {
			@Override
			public void onMessage(String user, String message) {
				if (user.equalsIgnoreCase(currentContact))
					messages.getItems().add(user + " : " + message);
			}
		});
	}
	private void loadContact(){
		if (currentContact != list.getSelectionModel().getSelectedItem()) {
			messages.getItems().clear();
			currentContact = list.getSelectionModel().getSelectedItem();
		}
	}
	public void load() {
		for (String user : Main.getUsers()) {
			if(!list.getItems().contains(user)) {
				list.getItems().add(user);
			}
		}
	}
	public void sendMessage() throws IOException {
		String from = Main.getCurUser().getUsername();
		String to = chooseContact();
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
	public String chooseContact() {
		loadContact();
		return list.getSelectionModel().getSelectedItem();
	}
	public void addMessage(String s){
		messages.getItems().add(s);
	}
}
