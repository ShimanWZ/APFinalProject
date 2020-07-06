package application;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
	private boolean searchState = false;
	
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
			//target.equalsIgnoreCase(user)
			if (user.startsWith(target)) {
				contactList.getItems().add(user);
			}
		}
	}
	@FXML private void play() throws IOException {
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
	@FXML private void chat() {
		
	}
	@FXML private void logout() throws IOException {
		String[] command = {"logout"};
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.window.close();
	}
}
