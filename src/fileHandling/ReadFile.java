package fileHandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

import base.User;

public class ReadFile {
	static class TicTacToeFile{

		static void readWinsAndLosses() {
			try {
				//opening the file and setting a scanner to iterate through file
				File file = new File("file.txt");
				Scanner reader = new Scanner(file);
				
				//reading and setting the lastgame's outcome
				if (reader.hasNext()) WriteFile.TicTacToeFile.setCurWinsCount(reader.nextInt());
				if (reader.hasNext()) WriteFile.TicTacToeFile.setCurLossesCount(reader.nextInt());
				
				reader.close();
			} catch (FileNotFoundException e) {
				System.out.println("writing a new file since it's your first game...");
			}
		}
	}
	public static class UsersFile{
		public static HashMap<String, User> readUsers(){
			try {
				FileInputStream inputStream = new FileInputStream("server.dat");
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				HashMap<String, User> users = (HashMap<String, User>)(objectInputStream.readObject());
				
				objectInputStream.close();
				return users;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} return null;
		}
	}
}
