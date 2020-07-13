package application;



//#101820ff
//#f2AA4cff




import java.io.IOException;
import java.util.ArrayList;
import base.Message;
import base.MessageListener;
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
	
	@SuppressWarnings("unchecked")
	@FXML private void signIn() throws IOException, ClassNotFoundException {
		String userName = username.getText();
		String passWord = password.getText();
		
		String errLogString = User.signIn(userName, passWord);
		
		if (errLogString.equalsIgnoreCase("yes")) {
			//recieving some importants properties form server
			Object[] objects = Server.getUsersProperties(Main.getObjOut(), Main.getObjIn());
			Main.setUsers((ArrayList<String>) objects[0]);
			Main.setCurUser((User) objects[1]);
			System.out.println(userName);
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
			
			User.messageReader();
			
			//changing scene
			Parent root = FXMLLoader.load(getClass().getResource("MainPageScene.fxml"));
			Main.mainScene = new Scene(root);
			Main.mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Main.window.setTitle(userName);
			Main.window.setScene(Main.mainScene);
			
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
	
}
