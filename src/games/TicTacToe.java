package games;

import java.util.Random;

public class TicTacToe {
	private TicTacToeContent[][] board;
	private boolean EOGame = false;
	private int winner = 0; // if the current player is the winner this variable will be 1
	private int lastComputerI, lastComputerJ;
	
	
	public TicTacToe() {
		board = new TicTacToeContent[3][3];
		EOGame = false;
	}
	
	
	
	public void computerTurn() {
		Random rand = new Random();
		boolean placed = false;
		
		while(!placed && !EOGame) {
			//generating random numbers to place the computers turn
			int i = Math.abs(rand.nextInt() % 3);
			int j = Math.abs(rand.nextInt() % 3);
			
			//checks if the room is empty, then places the O ;
			if (isValid(i,j)) {
				board[i][j] = TicTacToeContent.O;
				lastComputerI = i;
				lastComputerJ = j;
				if (checkForWin()) {
					winner = -1;
					System.out.println("Game over!");
				}
				placed = true;
			}
		}	
	}
	
	
	public boolean setOnBoard(int curI, int curJ, TicTacToeContent content) {
		if (isValid(curI, curJ) && !EOGame) {
			
			board[curI][curJ] = content;
			if (checkForWin()) {
				System.out.println("U won!");
				winner = 1;
			}
			return true;
		}
		return false;
	}
	
	
	private boolean checkForWin() {
		if((board[0][0] == board[0][1] && board[0][0] == board[0][2] && board[0][0] != null) 
		|| (board[1][0] == board[1][1] && board[1][0] == board[1][2] && board[1][0] != null) 
		|| (board[2][0] == board[2][1] && board[2][0] == board[2][2] && board[2][0] != null) 
		|| (board[0][0] == board[1][0] && board[0][0] == board[2][0] && board[0][0] != null) 
		|| (board[0][1] == board[1][1] && board[0][1] == board[2][1] && board[0][1] != null) 
		|| (board[0][2] == board[1][2] && board[0][2] == board[2][2] && board[0][2] != null)
		|| (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != null) 
		|| (board[2][0] == board[1][1] && board[2][0] == board[0][2] && board[2][0] != null)) {
			
			EOGame = true;
			return true;
		}
		else if(isBoardFull()) {
			EOGame = true;
			winner = 0;
			System.out.println("you tied");	
			return false;
		}
		return false;
	}
	private boolean isValid(int i, int j) {
		if(board[i][j] == null) return true;
		return false;
	}
	private boolean isBoardFull() {
		for(int i = 0 ; i< 3; i++) {
			for(int j = 0 ; j < 3 ; j++) {
				if (board[i][j] == null) return false;
			}
		}
		return true;
	}

	
	
	public boolean endOfGame() {
		return EOGame;
	}
	public int getLastComputerJ() {
		return lastComputerJ;
	}
	public int getLastComputerI() {
		return lastComputerI;
	}
	public int getWinner() {
		return winner;
	}
}
