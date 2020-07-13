package application;
	
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import base.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;


public class Main extends Application {
	private static User curUser;
	private static String contact;
	private static ArrayList<String> users;
	private static boolean isGameWithAI = true;
	
	private static Server server;
	private static Socket socket  = null;
	private static OutputStream outputStream;
	private static InputStream inputStream;
	private static BufferedReader bufferedReader;
	private static BufferedOutputStream bufferedOutputStream;
	private static ObjectOutputStream objOut;
	private static ObjectInputStream objIn;
	
	public static Stage window, game = new Stage();
	public static Scene main, tictactoe, signup, forgetPassword, chatScene, mainScene, privateMessageScene;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			window.setTitle("app");
			Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			main = new Scene(root);
			main.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			window.setScene(main);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//---- main -----
	public static void main(String[] args) {
		try {
			socket = new Socket("127.0.0.1", 8808);
			initializeInputOutput(socket);
		} catch (IOException e) {e.printStackTrace();}
		launch(args);
	}
		
	//-------------------------getters & setters ----------------------------//
	public static Socket getSocket() {
		return socket;
	}
	public static User getCurUser() {
		return curUser;
	}
	public static void setCurUser(User curUser) {
		Main.curUser = curUser;
	}
	public static Server getServer() {
		return server;
	}
	public static void setServer(Server server) {
		Main.server = server;
	}
	public static OutputStream getOutputStream() {
		return outputStream;
	}
	public static BufferedReader getBufferedReader() {
		return bufferedReader;
	}
	public static BufferedOutputStream getBufferedOutputStream() {
		return bufferedOutputStream;
	}
	public static ObjectOutputStream getObjOut() {
		return objOut;
	}
	public static ObjectInputStream getObjIn() {
		return objIn;
	}
	public static Stage getGameStage() {
		return game;
	}
	public static void setGameStage(Stage game) {
		Main.game = game;
	}
	public static ArrayList<String> getUsers() {
		return users;
	}
	public static void setUsers(ArrayList<String> users) {
		Main.users = users;
	}
	public static String getContact() {
		return contact;
	}
	public static void setContact(String contact) {
		Main.contact = contact;
	}
	public static boolean getIsGameWithAI() {
		return isGameWithAI;
	}
	public static void setGameWithAI(boolean isGameWithAI) {
		Main.isGameWithAI = isGameWithAI;
	}
	private static void initializeInputOutput(Socket socket) throws IOException {
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
		bufferedOutputStream = new BufferedOutputStream(outputStream);
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		objOut = new ObjectOutputStream(outputStream);
		objIn = new ObjectInputStream(inputStream);
	}
}
