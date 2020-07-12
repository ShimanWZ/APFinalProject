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
import java.util.LinkedList;
import java.util.Map;

import application.Main;
import base.Message;
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
					handleOffline();
				}else if(commandArray[0].equalsIgnoreCase("addcontact")) {
					handleAddingContact(commandArray);
				}else if (commandArray[0].equalsIgnoreCase("online")) {
					handleOnline(commandArray[1]);
				}else if(commandArray[0].equalsIgnoreCase("deleteMsges")) {
					handleDeletingMassege(commandArray);
				}else if(commandArray[0].equalsIgnoreCase("gameProperties")) {
					handleRecievingProperties(commandArray);
				}else if(commandArray[0].equalsIgnoreCase("gameFinished")) {
					handleGameFinished(commandArray);
				}
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private void handleGameFinished(String[] commandArray) {
		User thisUser = server.getUsers().get(this.getUsername());
		if (commandArray[1].equalsIgnoreCase("true")) {
			if (commandArray[2].equalsIgnoreCase("true")) {
				thisUser.setTictactoeCompWins(thisUser.getTictactoeCompWins() + 1);
			}else {
				thisUser.setTictactoePlayWins(thisUser.getTictactoePlayWins() + 1);
			}
		}else {
			if (commandArray[2].equalsIgnoreCase("true")) {
				thisUser.setTictactoeCompLosses(thisUser.getTictactoeCompLosses() + 1);
			}else {
				thisUser.setTictactoePlayLosses(thisUser.getTictactoePlayLosses() + 1);
			}
		}
		WriteFile.UsersFile.writeUsers(server.getUsers());
	}
	private void handleRecievingProperties(String[] commandArray) throws IOException {
		String reciever = commandArray[1];
		for (Client client: server.getClients()) {
			if (client.getUsername().equals(reciever)){
				String[] command = new String[3];
				command[0] = commandArray[0];
				command[1] = commandArray[2];
				command[2] = commandArray[3];
				client.getObjectOS().writeObject(command);
				client.getObjectOS().flush();
			}
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
			System.out.println(curUser);
			String pass = curUser.getPassword();
			if (pass.equals(password)) {
				this.username = username;
				outputStream.write("ok\n".getBytes());				
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
		Object[] arrays = {usersList, server.getUsers().get(this.username)};
		
		objectOS.writeObject(arrays);
		objectOS.flush();
	}
	private void handleSendingMessage(String[] commandArray) throws IOException {
		String sender = commandArray[1];
		String reciever = commandArray[2];
		String message = commandArray[3];
		handleSavingMessage(sender, reciever, message);
		for (Client client: server.getClients()) {
			if (client.getUsername().equals(reciever)){
				String[] command = new String[3];
				command[0] = "recieve";
				command[1] = sender;
				command[2] = message;
				client.getObjectOS().writeObject(command);
				client.getObjectOS().flush();
			}
		}
	}
	private void handleLogOut() throws IOException {
		server.getClients().remove(this);
		this.socket.close();
	}
	private void handleAddingContact(String[] commandArray) {
		String user = commandArray[1];
		String addUser = commandArray[2];
		
		User thisUser = server.getUsers().get(user);
		thisUser.addContact(addUser);
		WriteFile.UsersFile.writeUsers(server.getUsers());
		System.out.println("contact added");
	}
	private void handleSavingMessage(String sender, String reciever, String text){
		Message message = new Message(sender, reciever, text);
		System.out.println("messageCreated!");
		boolean flag1 = false, flag2 = false;
		for (Map.Entry entry : server.getUsers().entrySet()) {
			System.out.println("looping... " + entry.getKey());
			if (entry.getKey().equals(sender)) {
				User senderUser =(User)entry.getValue();
				senderUser.addMessage(message, reciever);
				flag1 = true;
				System.out.println("firstif");
			}
			else if (entry.getKey().equals(reciever)) {
				User recieverUser =(User)entry.getValue();
				recieverUser.addMessage(message, sender);
				flag2 = true;
				System.out.println("second if");
			}
			if (flag1 && flag2) {
				System.out.println("exit looping");
				break;
			}
		}
		WriteFile.UsersFile.writeUsers(server.getUsers());
	}
	private void handleOffline() throws IOException {
		for (Client client : server.getClients()) {
			String[] command = {"offline", this.username};
			client.getObjectOS().writeObject(command);
			client.getObjectOS().flush();
		}
	}
	private void handleOnline(String username) throws IOException {
		String[] command = {"online", username};
		for (Client client : server.getClients()) {
			if (!client.equals(this)) {
				client.getObjectOS().writeObject(command);
				client.getObjectOS().flush();
			}
		}
	}
	private void handleDeletingMassege(String[] commandArray) {
		for (Map.Entry entry : server.getUsers().entrySet()) {
			if (entry.getKey().equals(commandArray[1])) {
				User thisuser = (User) entry.getValue();
				LinkedList<Message> target = null;
				for(LinkedList<Message> privateChat : thisuser.getMessages()) {
					if (privateChat.getFirst().getReciever().equalsIgnoreCase(commandArray[2]) 
							|| privateChat.getFirst().getSender().equalsIgnoreCase(commandArray[2])) {
						target = privateChat;
					}
				}
				thisuser.getMessages().remove(target);
			}
			else if (entry.getKey().equals(commandArray[2])) {
				User thisuser = (User) entry.getValue();
				LinkedList<Message> target = null;
				for(LinkedList<Message> privateChat : thisuser.getMessages()) {
					if (privateChat.getFirst().getReciever().equalsIgnoreCase(commandArray[1]) 
							|| privateChat.getFirst().getSender().equalsIgnoreCase(commandArray[1])) {
						target = privateChat;
					}
				}
				thisuser.getMessages().remove(target);
			}
		}
	}
	
	//---------------------------------------------getter methods-----------------------------------//
	public ObjectOutputStream getObjectOS() {
		return this.objectOS;
	}
	private String getUsername() {
		return username;
	}
}
