package application;
import java.io.IOException;
import java.util.ArrayList;
import base.Message;
import base.MessageListener;
import base.User;
import base.UserStatusListener;
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
	
	@FXML private void signIn() throws IOException, ClassNotFoundException {
		String userName = username.getText();
		String passWord = password.getText();
		
		String errLogString = User.signIn(userName, passWord);
		
		if (errLogString.equalsIgnoreCase("yes")) {
			//recieving some importants properties form server (this user, all users, online users)
			setInitialProperties();
			//adding listeners for recieving messages and online offline status...
			addListenersToCurUser();
			//running the server responds handler thread....
			User.messageReader();
			//changing scene
			Parent root = FXMLLoader.load(getClass().getResource("PrivateMessages.fxml"));
			Main.mainScene = new Scene(root);
			Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.window.setTitle(userName);
			Main.window.setScene(Main.mainScene);
			//tellin other users that Im online!!
			sendMyOnlineProperties();
		}
		else errorLog.setText(errLogString);
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
	//-------------------------- privete methods--------------------------//
	private void addListenersToCurUser() {
		//adding a message listener for unread messages.
		Main.getCurUser().addMessageListener(new MessageListener() {
			@Override
			public void onMessage(String user, String message) {
				addMessagetoLinkedList(user, message);
			}

			private void addMessagetoLinkedList(String user, String message) {
				Main.getCurUser().addMessage(new Message(user, Main.getCurUser().getUsername(), message), user);
			}
		});
		//adding userstatuslistener for online and offline
		Main.getCurUser().addUserStatusListener(new UserStatusListener() {
			
			@Override
			public void online(String username) {
				Main.addOnlineUser(username);
			}
			
			@Override
			public void offline(String username) {
				Main.removeOnlineUser(username);
			}
		});
	}
	@SuppressWarnings("unchecked")
	private void setInitialProperties() throws ClassNotFoundException, IOException {
		Object[] objects = Server.getUsersProperties(Main.getObjOut(), Main.getObjIn());
		Main.setUsers((ArrayList<String>) objects[0]);
		Main.setCurUser((User) objects[1]);
		Main.setOnlineUsers((ArrayList<String>) objects[2]);
	}
	private void sendMyOnlineProperties() throws IOException {
		String[] command  = {"online" , Main.getCurUser().getUsername()};
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
	}
}
