package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingController {
	@FXML private TextField curPass;
	@FXML private TextField newPass;
	@FXML private TextField passConfirm;
	@FXML private Label passLabel;
	@FXML private TextField email;
	@FXML private Label emailLabel;
	private boolean init = false;
	
	
	@FXML private void changePassScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("changePassScene.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
	@FXML private void changeEmailScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ChangeEmailScene.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
	@FXML private void changeQuestScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("changePassQuestScene.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
	@FXML private void changePass() throws IOException {
		String curPassword = curPass.getText();
		String newPassword = newPass.getText();
		String passConfirmation = passConfirm.getText();
		
		if (curPassword.equals(Main.getCurUser().getPassword())) {
			if (newPassword.equals(passConfirmation)) {
				handleChangingPassword(newPassword);
			} else passLabel.setText("passwords dont match!");
		} else passLabel.setText("wrong password");
				
	}
	private void handleChangingPassword(String newPassword) throws IOException {
		String[] command = new String[3];
		command[0] = "setting";
		command[1] = "password";
		command[2] = newPassword;
		
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.getCurUser().setPassword(newPassword);
	}
	@FXML private void changeEmail() throws IOException {
		String emailAddress = email.getText();
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	    if (emailAddress.matches(regex)) {
	    	handleChangingEmail(emailAddress);
	    } else emailLabel.setText("wrong email format!");
	}
	private void handleChangingEmail(String emailAddress) throws IOException {
		String[] command = new String[3];
		command[0] = "setting";
		command[1] = "email";
		command[2] = emailAddress;
		
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
		Main.getCurUser().setEmailAddress(emailAddress);
	}
	@FXML private void changeQuestion() {
		
	}
	@FXML private void loadChoiceBox() {
		
	}
	@FXML private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
}
