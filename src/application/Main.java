package application;
	

import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	private static Socket socket  = null; 
	public static Stage window;
	public static Scene main, tictactoe, signup, forgetPassword;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			main = new Scene(root);
			main.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			window.setScene(main);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			socket = new Socket("127.0.0.1", 8808);
		} catch (IOException e) {e.printStackTrace();}
		
		launch(args);
	}
	public static Socket getSocket() {
		return socket;
	}
}
