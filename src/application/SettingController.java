package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingController {
	@FXML private TextField curPass;
	@FXML private TextField newPass;
	@FXML private TextField passConfirm;
	@FXML private Label passLabel;
	@FXML private TextField email;
	@FXML private Label emailLabel;
	@FXML private TextField questAns;
	@FXML private ChoiceBox<String> questionChoice;
	private boolean choiceBoxLoaded = false;
	
	//-----------------------------------------------------------------//
	//-------------the next three methods change scenes----------------//
	//-----------------------------------------------------------------//
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
	
	//-----------------------------------------------------------------//
	//-------------the next two methods change password----------------//
	//-----------------------------------------------------------------//
	
	@FXML private void changePass() throws IOException {
		String curPassword = curPass.getText();
		String newPassword = newPass.getText();
		String passConfirmation = passConfirm.getText();
		
		if (curPassword.equals(Main.getCurUser().getPassword())) {
			if (newPassword.equals(passConfirmation)) {
				handleChangingPassword(newPassword);
				passLabel.setText("password changed successfully");
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
	//-----------------------------------------------------------------//
	//-------------the next two methods change email ------------------//
	//-----------------------------------------------------------------//
	@FXML private void changeEmail() throws IOException {
		String emailAddress = email.getText();
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	    if (emailAddress.matches(regex)) {
	    	handleChangingEmail(emailAddress);
	    	emailLabel.setText("email changed successfully");
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
	//-----------------------------------------------------------------//
	//-------the next two methods handle changing pass question--------//
	//-----------------------------------------------------------------//
	@FXML private void changeQuestion() throws IOException {
		String quesString = questionChoice.getSelectionModel().getSelectedItem();
		String ansString;
		if(!questAns.getText().isEmpty()) {
			ansString = questAns.getText();
			String[] command = new String[4];
			command[0] = "setting";
			command[1] = "question";
			command[2] = quesString;
			command[3] = ansString;
			
			Main.getObjOut().writeObject(command);
			Main.getObjOut().flush();
			
			Main.getCurUser().setPasswordQuestion(quesString);
			Main.getCurUser().setPasswordAnswer(ansString);
		}
	}
	@FXML private void loadChoiceBox() {
		if (!choiceBoxLoaded ) {
			questionChoice.getItems().addAll("favorit color?", "favorite fruit?", "favorite flower?");
			choiceBoxLoaded = true;
		}
	}
	
	@FXML private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
		Main.mainScene = new Scene(root);
		Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setTitle(Main.getCurUser().getUsername());
		Main.window.setScene(Main.mainScene);
	}
}
