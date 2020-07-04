package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import base.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ForgetPasswordController {
	@FXML private Label questionField;
	@FXML private Label passwordField;
	@FXML private TextField answerField;
	@FXML private TextField username;
	
	//-----------------------------FX methods----------------------------------//
	@FXML private void passwordAndQuestionFieldController() throws IOException, ClassNotFoundException {
		ObjectOutputStream objOut = Main.getObjOut();
		ObjectInputStream objIn = Main.getObjIn();
		String userName = username.getText();
		
		sendServerProperties(objOut, userName);
		
		//revieve this user's data
		User temp = (User)objIn.readObject();
		
		if(temp != null) {
			questionField.setText(temp.getPasswordQuestion());
			answerField.setDisable(false);
			
			if (answerField.getText().equals(temp.getPasswordAnswer())) 
				passwordField.setText("your password is : " + temp.getPassword());
			else passwordField.setText("please enter a valid answer");
		}
	}
	@FXML private void back() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
		Main.main = new Scene(root);
		Main.main.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.window.setScene(Main.main);
	}
	//-------------------------------private helper methods----------------------------------//
	private void sendServerProperties(ObjectOutputStream objOut, String username) throws IOException {
		//initializing request array
		String[] request = new String[2];
		request[0] = "forget";
		request[1] = username;
			
		//sending request
		objOut.writeObject(request);
		objOut.flush();
	}
}
