package fileHandling;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import application.Main;
import games.TicTacToe;
import server.Server;

public class WriteFile {
	public static class TicTacToeFile{

		private static int curWinsCount = 0 , curLossesCount = 0;
		
		
		public static void writeWinsAndLosses(TicTacToe game) {
			  
			try {
				//readeing the file to get the current wins and losses count
				ReadFile.TicTacToeFile.readWinsAndLosses();
				
				//creating a file writer to overwrite the file
				FileWriter fr = new FileWriter("file.txt");
				BufferedWriter br = new BufferedWriter(fr);
		        PrintWriter out = new PrintWriter(br);
		        
		        
		        //some debugging shit
		        System.out.println(curWinsCount);
		        System.out.println(curLossesCount);
		        
		        
		        
		        if (game.getWinner() == 1) { //you are the winner
		        	// Main.getCurUser().setTictactoeCompWins(Main.getCurUser().getTictactoeCompWins() + 1);
		        	out.write(curWinsCount + 1 + " ");
		        	 out.write(curLossesCount + " ");
		        }
		        else if (game.getWinner() == -1) { //the opponent is the winnet
		        	// Main.getCurUser().setTictactoeCompLosses(Main.getCurUser().getTictactoeCompLosses() + 1);
		        	out.write(curWinsCount + " ");
			        out.write(curLossesCount + 1 + " ");
		        }
		        else { // you tied!
		        	out.write(curWinsCount + " ");
		        	out.write(curLossesCount + " ");
		        }
		       
		        
		        out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	         
		}

		public static void setCurLossesCount(int curLossesCount) {
			TicTacToeFile.curLossesCount = curLossesCount;
		}
		public static void setCurWinsCount(int curWinsCount) {
			TicTacToeFile.curWinsCount = curWinsCount;
		}
	}
	public static class UsersFile{
		public static void writeUsers(Object users){
			
			try {
				FileOutputStream outputStream = new FileOutputStream("server.dat");
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				
				objectOutputStream.writeObject(users);
				
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
