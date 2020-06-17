package application;

import java.io.IOException;

import base.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.Server;

public class LoginHandler {
	@FXML private PasswordField password;
	@FXML private TextField username;
	@FXML private Label errorLog;
	
	@FXML private void signIn() throws IOException {
		String userName = username.getText();
		String passWord = password.getText();
		
		if(Server.getUsers().containsKey(userName)) {
			User curUser = Server.getUsers().get(userName);

			if (passWord.equals(curUser.getPassword())) {
				Parent root = FXMLLoader.load(getClass().getResource("tictactoe.fxml"));
				Main.tictactoe = new Scene(root);
				Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Main.window.setScene(Main.tictactoe);
			}
			else errorLog.setText("Wrong password");
		}
		else errorLog.setText("no user found!");
	}
	
	@FXML private void signUp() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SignupScene.fxml"));
		Main.signup = new Scene(root);
		Main.signup.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.signup);
	}
	@FXML private void forgetPassword() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("forgetpasswordScene.fxml"));
		Main.forgetPassword = new Scene(root);
		Main.forgetPassword.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.forgetPassword);
		
	}
}
