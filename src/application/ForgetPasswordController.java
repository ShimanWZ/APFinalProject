package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.Server;

public class ForgetPasswordController {
	@FXML private Label questionField;
	@FXML private Label passwordField;
	@FXML private TextField answerField;
	@FXML private TextField username;
	
	
	@FXML private void passwordAndQuestionFieldController() {
		if(Server.getUsers().containsKey(username.getText())) {
			questionField.setText(Server.getUsers().get(username.getText()).getPasswordQuestion());
			answerField.setDisable(false);
			if (answerField.getText().equals(Server.getUsers().get(username.getText()).getPasswordAnswer())) 
				passwordField.setText("your password is : " + Server.getUsers().get(username.getText()).getPassword());
			else passwordField.setText("please enter a valid answer");
		}
	}
	@FXML private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
		Main.main = new Scene(root);
		Main.main.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.main);
	}
}
