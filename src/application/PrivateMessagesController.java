package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import base.Message;
import base.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PrivateMessagesController {
	@FXML ListView<Message> chatList;
	@FXML ListView<Integer> unreadCount;
	
	private boolean init = false;
	
	@FXML private void initializeChats() {
		if (!init) {
			chatList.getItems().clear();
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
	@FXML private void choosePrivateMEssage(MouseEvent event) throws IOException {
		if (event.getClickCount()> 1 && chatList.getSelectionModel().getSelectedItem()!= null) {
			Message msg = chatList.getSelectionModel().getSelectedItem();
			String thisContact = (msg.getReciever().equalsIgnoreCase(Main.getCurUser().getUsername()) ? msg.getSender() : msg.getReciever());
			Main.setContact(thisContact);
			
			Stage chatStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("MainChatScene.fxml"));
			Main.tictactoe = new Scene(root);
			Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			chatStage.setScene(Main.tictactoe);
			chatStage.show();
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
		Main.getCurUser().getMessages().remove(target);
	}
	private Integer countUnread(LinkedList<Message> privateChat) {
		Iterator<Message> iterator = privateChat.iterator();
		int count = 0;
		if (privateChat.getLast().isRead()) return count;
		else {
			while (iterator.hasNext()) {
				Message msg = iterator.next();
				if (!msg.isRead()) count++;
			}
		}
		return count;
	}
}
