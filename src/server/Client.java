package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import base.User;
import base.UserStatusListener;
import fileHandling.WriteFile;

public class Client extends Thread{
	private String username;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedReader bufferedReader;
	private ObjectInputStream objectIS;
	private ObjectOutputStream objectOS;
	private Server server;
	
	//-----------------------------constructor----------------------------------//
	public Client(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		try {
			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();
			this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			this.objectIS = new ObjectInputStream(inputStream);
			this.objectOS = new ObjectOutputStream(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//-----------------------------------------------------------------------//
	@Override
	public void run() {
		String[] commandArray;
		
		try {
			while ((commandArray = (String[])(objectIS.readObject())) != null) {
				if (commandArray[0].equalsIgnoreCase("login")) {
					handleLogin(commandArray);
				}
				else if (commandArray[0].equalsIgnoreCase("forget")) {
					handleForgetPassword(commandArray);
				}
				else if (commandArray[0].equalsIgnoreCase("signup")) {
					handleSignup();
				}
				else if(commandArray[0].equalsIgnoreCase("getusers")) {
					handleGetUsersList();
				}
				else if(commandArray[0].equalsIgnoreCase("send")) {
					handleSendingMessage(commandArray);
				}else if(commandArray[0].equalsIgnoreCase("logout")) {
					handleLogOut();
				}
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	
	//----------------------------helper methods---------------------------------//
	private void handleSignup() throws ClassNotFoundException, IOException {
		User temp = (User) objectIS.readObject();
		if (server.getUsers().containsKey(temp.getUsername())) {
			outputStream.write("username you've chosen is taken\n".getBytes());
		}
		else {
			server.getUsers().put(temp.getUsername(), temp);
			WriteFile.UsersFile.writeUsers(server.getUsers());
			outputStream.write("signed up successfully\n".getBytes());
		}
		outputStream.flush();
	}
	private void handleLogin(String[] commandArray) throws IOException {
		String username = commandArray[1];
		String password = commandArray[2];
		if (server.getUsers().containsKey(username)){
			User curUser = server.getUsers().get(username);

			String pass = server.getUsers().get(username).getPassword();
			if (pass.equals(password)) {
				this.username = username;
				//sending servers response
				outputStream.write("ok\n".getBytes());
				//sending users object to user
				objectOS.writeObject(curUser);
				objectOS.flush();
			}else {
				outputStream.write("wrongPass\n".getBytes());
			}
		} else {
			outputStream.write("noUserFound\n".getBytes());
		}
	}
	private void handleForgetPassword(String[] commandArray) throws IOException {
		String username = commandArray[1];
		if (server.getUsers().containsKey(username)) {
			User curUser = server.getUsers().get(username);
			objectOS.writeObject(curUser);
			objectOS.flush();
		}
		else {
			objectOS.writeObject(null);
			objectOS.flush();
		}
	}
	private void handleGetUsersList() throws IOException {
		ArrayList<String> usersList = new ArrayList<>();
		
		for (Map.Entry entry : server.getUsers().entrySet()) {
			usersList.add((String)entry.getKey());
		}
		objectOS.writeObject(usersList);
		objectOS.flush();
	}
	private void handleSendingMessage(String[] commandArray) throws IOException {
		String from = commandArray[1];
		String reciever = commandArray[2];
		String message = commandArray[3];
		
		for (Client client: server.getClients()) {
			if (client.getUsername().equals(reciever)){
				String[] command = new String[3];
				command[0] = "recieve";
				command[1] = from;
				command[2] = message;
				client.getObjectOS().writeObject(command);
			}
		}
	}
	private void handleLogOut() throws IOException {
		server.getClients().remove(this);
		this.socket.close();
	}
	//---------------------------------------------getter methods-----------------------------------//
	public ObjectOutputStream getObjectOS() {
		return this.objectOS;
	}
	private String getUsername() {
		return username;
	}
}
