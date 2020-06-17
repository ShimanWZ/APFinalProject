package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
import base.User;
import fileHandling.ReadFile;

public class Server {
	private int port ;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private boolean serverRunning;
	private static HashMap<String, User> Users = new HashMap<String, User>();
	
	public Server(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
			serverRunning = true;
			System.out.println("serverStarterd");
			
			
			
			Thread connectionThread = new Thread( new Runnable() {
				public void run() {
					while (serverRunning) {
						try {
							System.out.println("waiting for connection...");
							socket = serverSocket.accept();
							System.out.println("connected : " + socket.toString());
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			});
			
			connectionThread.start();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static HashMap<String, User> getUsers() {
		return Users;
	}
	@SuppressWarnings("unchecked")
	public static void setUsers(HashMap<String, User> users) {
		Users = (HashMap<String, User>) ReadFile.UsersFile.readUsers();
	}
	public void addUser(User user) {
		Users.put(user.getUsername() , user);
	}

	//----------------------- main ------------------------//
	public static void main(String[] args) {
		Server server = new Server(8808);
	}




}
