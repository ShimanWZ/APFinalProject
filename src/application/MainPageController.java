package application;

import java.io.IOException;
import java.io.ObjectOutputStream;

import base.UserStatusListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainPageController {
	@FXML private ListView<String> contactList;
	@FXML private TextField searchbar;
	@FXML private ListView<String> onlineUsers;
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
		for (String user : Main.getCurUser().getContacts()) {
			if(!contactList.getItems().contains(user) && !user.equalsIgnoreCase(Main.getCurUser().getUsername()) && !searchState) {
				contactList.getItems().add(user);
			}
		}
	}
	@FXML private void chooseContact(MouseEvent event) throws IOException {
		if (event.getClickCount()> 1 && contactList.getSelectionModel().getSelectedItem()!= null) {
			Main.setContact(contactList.getSelectionModel().getSelectedItem());
			Stage chatStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("MainChatScene.fxml"));
			Main.tictactoe = new Scene(root);
			Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			chatStage.setScene(Main.tictactoe);
			chatStage.show();
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
	}
	@FXML private void play() throws IOException {
		Main.setGameWithAI(false);
		Main.setContact(contactList.getSelectionModel().getSelectedItem());
		Stage gameStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("tictactoe.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		gameStage.setScene(Main.tictactoe);
		gameStage.show();
	}
	@FXML private void exitSearchMode() {
		searchState = false;
		contactList.getItems().clear();
	}
	@FXML private void chat() throws IOException {
		Stage privateMEssages = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		privateMEssages.setScene(Main.tictactoe);
		privateMEssages.show();
	}
	@FXML private void logout() throws IOException {
		String[] command = {"logout"};
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.window.close();
	}
}
