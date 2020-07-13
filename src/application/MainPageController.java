package application;

import java.io.IOException;
import java.io.ObjectOutputStream;

import base.UserStatusListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainPageController {
	@FXML private ListView<String> contactList;
	@FXML private TextField searchbar;
	@FXML private ListView<String> onlineUsers;
	@FXML private Button addContacts;
	@FXML private Label username;
	private boolean searchState = false;
	
	
	{
		Main.getCurUser().addUserStatusListener(new UserStatusListener() {
			@Override
			public void online(String username) {
				onlineUsers.getItems().add(username);
			}
			
			@Override
			public void offline(String username) {
				onlineUsers.getItems().remove(username);
			}
		});
	}
	
	@FXML private void load() {
		username.setText(Main.getCurUser().getUsername());
		for (String user : Main.getCurUser().getContacts()) {
			if(!contactList.getItems().contains(user) && !user.equalsIgnoreCase(Main.getCurUser().getUsername()) && !searchState) {
				contactList.getItems().add(user);
			}
		}
	}
	@FXML private void chooseContact(MouseEvent event) throws IOException {
		if (event.getClickCount()> 1 && contactList.getSelectionModel().getSelectedItem()!= null) {
			String contact = contactList.getSelectionModel().getSelectedItem();
			if (Main.getCurUser().getContacts().contains(contact)) {
				Main.setContact(contact);
				Parent root = FXMLLoader.load(getClass().getResource("MainChatScene.fxml"));
				Main.tictactoe = new Scene(root);
				Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Main.window.setScene(Main.tictactoe);
			}
		}
	}
	@FXML private void addContact() throws IOException {
		String username = contactList.getSelectionModel().getSelectedItem();
		ObjectOutputStream out = Main.getObjOut();
		if(username != null) {
			String[] command = {"addcontact", Main.getCurUser().getUsername(), username};
			out.writeObject(command);		
			out.flush();
		}
		Main.getCurUser().addContact(username);
		searchState = false;
		addContacts.setDisable(true);
		contactList.getItems().clear();
	}
	@FXML private void search() {
		contactList.getItems().clear();
		searchState = true;
		String target = searchbar.getText();
		for (String user : Main.getUsers()) {
			if (user.startsWith(target)) {
				contactList.getItems().add(user);
			}
		}
		addContacts.setDisable(false);
	}
	@FXML private void play() throws IOException {
		Main.setGameWithAI(false);
		Main.setContact(contactList.getSelectionModel().getSelectedItem());
		Parent root = FXMLLoader.load(getClass().getResource("tictactoe.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.getGameStage().setScene(Main.tictactoe);
		Main.getGameStage().show();
	}
	@FXML private void exitSearchMode() {
		searchState = false;
		addContacts.setDisable(true);
		searchbar.setText("");
		contactList.getItems().clear();
	}
	@FXML private void chat() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
		Main.privateMessageScene = new Scene(root);
		Main.privateMessageScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.privateMessageScene);
	}
	@FXML private void logout() throws IOException {
		String[] command = {"logout"};
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.window.close();
	}
}
