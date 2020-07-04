package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import base.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupHandler {
	@FXML private TextField username;
	@FXML private TextField emailAddress;
	@FXML private TextField passwordAnswer; 
	@FXML private PasswordField password;
	@FXML private PasswordField passConfirm;
	@FXML private Label errorLog;
	@FXML private ChoiceBox<String> questionChoice;
	private boolean choiceBoxLoaded = false;
	
	
	//------------------------ fxml button and mouse listeners -----------------------------//
	@FXML
	private void signup() throws IOException {
		if (passConfirmation() && emailConfirmation() && !areFieldsEmpty()) {
			User currentUser = new User(username.getText(), password.getText(), emailAddress.getText());
			currentUser.setPasswordQuestion(questionChoice.getValue());
			currentUser.setPasswordAnswer(passwordAnswer.getText());
			
			sendServerPropetries(currentUser);
			handleServerResponse();	
		} else {
			errorLogHandler();
		}
	}
	@FXML 
	private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
		Main.main = new Scene(root);
		Main.main.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.main);
	}
	@FXML 
	private void loadChoiceBoxItems() {
		if (!choiceBoxLoaded) {
			questionChoice.getItems().addAll("favorit color?", "favorite fruit?", "favorite flower?");
			choiceBoxLoaded = true;
		}
	}
	//------------------------------ helper methods-------------------------------------//
	private boolean passConfirmation() {
		if (password.getText().equals(passConfirm.getText()) && password.getText().length() >= 8) return true;
		return false;
	}
	private boolean emailConfirmation() {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	    return emailAddress.getText().matches(regex);
	}
	private void errorLogHandler() {
		if (areFieldsEmpty()) errorLog.setText("no fields can be empty");
		else if (!passConfirmation()) {
			if (password.getText().length() < 8) errorLog.setText("password should be at least 8 chars!");
			else errorLog.setText("passwords don't match");
		}
		else if (!emailConfirmation()) errorLog.setText("please enter a valid email address");
	}
	private boolean areFieldsEmpty() {
		if (password.getText().isEmpty()) return true;
		if (username.getText().isEmpty()) return true;
		if (emailAddress.getText().isEmpty()) return true;
		if (passwordAnswer.getText().isEmpty()) return true;
		return false;	
	}
	//server related methods
	private void sendServerPropetries(User user) throws IOException {
		ObjectOutputStream objectOI = Main.getObjOut();
		String[] command = new String[1];
		command[0] = "signup";
		//sending array
		objectOI.writeObject(command);
		objectOI.flush();
		//sending user
		objectOI.writeObject(user);
		objectOI.flush();
	}
	private void handleServerResponse() throws IOException {
		BufferedReader reader = Main.getBufferedReader();
		String response = reader.readLine();
		errorLog.setText(response);
	}
}
