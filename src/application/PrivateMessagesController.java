package application;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PrivateMessagesController {
	@FXML private ListView<Message> chatList;
	@FXML private ListView<Integer> unreadCount;
	@FXML private Label username;
	private boolean init = false;
	
	{
		Main.getCurUser().addMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(String user, String message) {
				init = false;
				Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	initializeChats();
		            }
				});
			}
		});
	}
	
	
	@FXML private void initializeChats() {
		if (!init) {
			username.setText(Main.getCurUser().getUsername());
			chatList.getItems().clear();
			unreadCount.getItems().clear(); 
			User thisUser = Main.getCurUser();
			ArrayList<LinkedList<Message>> messages = thisUser.getMessages();
			
			
			for(LinkedList<Message> privateChat : messages) {
				Message lastMsg = privateChat.getLast();
				this.chatList.getItems().add(lastMsg);
				this.unreadCount.getItems().add(countUnread(privateChat));
			}
			init = true;
		}
	}
	private Integer countUnread(LinkedList<Message> privateChat) {
		Iterator<Message> iterator = privateChat.iterator();
		int count = 0;
		if (privateChat.getLast().isRead()) {
			return count;
		}
		else {
			while (iterator.hasNext()) {
				Message msg = iterator.next();
				if (!msg.isRead()) count++;
			}
		}
		return count;
	}
	
	
	@FXML private void choosePrivateMEssage(MouseEvent event) throws IOException {
		if (event.getClickCount()> 1 && chatList.getSelectionModel().getSelectedItem()!= null) {
			Message msg = chatList.getSelectionModel().getSelectedItem();
			String thisContact = (msg.getReciever().equalsIgnoreCase(Main.getCurUser().getUsername()) ? msg.getSender() : msg.getReciever());
			Main.setContact(thisContact);
			
			Parent root = FXMLLoader.load(getClass().getResource("MainChatScene.fxml"));
			Main.tictactoe = new Scene(root);
			Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.window.setScene(Main.tictactoe);
		}
	}
	@FXML private void deletePrivateMessage() throws IOException {
		Message msg = chatList.getSelectionModel().getSelectedItem();
		String thisContact = (msg.getReciever().equalsIgnoreCase(Main.getCurUser().getUsername()) ? msg.getSender() : msg.getReciever());
		LinkedList<Message> target = null;
		for(LinkedList<Message> privateChat : Main.getCurUser().getMessages()) {
			if (privateChat.getFirst().getReciever().equalsIgnoreCase(thisContact) || privateChat.getFirst().getSender().equalsIgnoreCase(thisContact)) {
				String[] command = {"deleteMsges", Main.getCurUser().getUsername(), thisContact};
				Main.getObjOut().writeObject(command);
				Main.getObjOut().flush();
				target = privateChat;
			}
		}
		Main.getCurUser().getMessages().remove(target); //removes targetted private chat from messages
		init = false;
		initializeChats();
	}
	@FXML private void playAI() throws IOException {
		Main.setGameWithAI(true);
		Parent root = FXMLLoader.load(getClass().getResource("tictactoe.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.getGameStage().setScene(Main.tictactoe);
		Main.getGameStage().show();
	}
	@FXML private void logout() throws IOException {
		String[] command = {"logout"};
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.window.close();
	}
	@FXML private void changeToContactsScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MainPageScene.fxml"));
		Main.privateMessageScene = new Scene(root);
		Main.privateMessageScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.privateMessageScene);
	}
	@FXML private void setting() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Settingscene.fxml"));
		Main.privateMessageScene = new Scene(root);
		Main.privateMessageScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.privateMessageScene);
	}
}
