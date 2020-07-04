package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainPageController {
	@FXML private ListView<String> contactList;
	@FXML private TextField searchbar;

	@FXML private void load() {
		for (String user : Main.getUsers()) {
			if(!contactList.getItems().contains(user) && !Main.getCurUser().getUsername().equalsIgnoreCase(user)) {
				contactList.getItems().add(user);
			}
		}
	}
	@FXML private void chooseContact() {
		
	}
	@FXML private void search() {
		
	}
	@FXML private void play() {
		
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
