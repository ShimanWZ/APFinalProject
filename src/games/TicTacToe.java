package games;

import java.io.IOException;
import application.Main;

public class TicTacToe {
	private TicTacToeContent[][] board;
	private TicTacToeContent computer = TicTacToeContent.O, player = TicTacToeContent.X;
	private boolean EOGame = false;
	private boolean AI = Main.getIsGameWithAI();
	private boolean myTurn = true;
	private int winner = -2; // winner: 1 , loser : -1, tie : 0
	private static int lastOpponentI = -1, lastOpponentJ = -1;
	
	
	public TicTacToe() {
		board = new TicTacToeContent[3][3];
		EOGame = false;
	}
	
	
	public void oponentTurn() throws IOException {
		bestMove bestMove = findBestMove(board); 
			  
		lastOpponentI = bestMove.bestI;
		lastOpponentJ = bestMove.bestJ;
		
		if(lastOpponentI != -1) board[lastOpponentI][lastOpponentJ] = TicTacToeContent.O;
			
		if (checkForWin()) {
			this.winner = -1;
			sendGameProperties(-1, true);
		}
		
	}
	
	public boolean setOnBoard(int curI, int curJ, TicTacToeContent content) throws IOException {
		if (isValid(curI, curJ) && !EOGame) {
			
			board[curI][curJ] = content;

			if (!AI) sendProperties(curI, curJ);
			if (checkForWin()) {
				sendGameProperties(1, AI);
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
			return false;
		}
		return false;
	}
	private boolean isValid(int i, int j) {
		if(board[i][j] == null && (i!= lastOpponentI || j != lastOpponentJ)) return true;
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
	//----------------------------- setters and getters... ------------------------------//
	public boolean endOfGame() {
		return EOGame;
	}
	public int getLastOpponentJ() {
		return lastOpponentJ;
	}
	public int getLastOpponentI() {
		return lastOpponentI;
	}
	public static void setLastOponentI(int lastI) {
		lastOpponentI = lastI;
	}
	public static void setLastOponentJ(int lastJ) {
		lastOpponentJ = lastJ;
	}
	public int getWinner() {
		return winner;
	}
	public void setAI(boolean aI) {
		AI = aI;
	}	
	public boolean isAI() {
		return AI;
	}
	public boolean isMyTurn() {
		return myTurn;
	}
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
	public boolean getIfOpponentWon() throws IOException {
		if (checkForWin()) {
			winner = -1;
			sendGameProperties(-1, false);
			return true;
		}
		return false;
	}
	public void setOpponentOnBord(int curI, int curJ) {
		board[curI][curJ] = TicTacToeContent.O;
	}
	//--------------------------------------------------------------------------//
	//--------------------------server handling---------------------------------//
	//--------------------------------------------------------------------------//
	// when a move is done this method sends the current
	//move to the opponent....
	private void sendProperties(int curI, int curJ)  {
		String[] s = {"gameProperties", Main.getContact(), curI + "", curJ + ""};
		try {
			Main.getObjOut().writeObject(s);
			Main.getObjOut().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// when the game's ended, this method will send the
	// properties to the server....
	public static void sendGameProperties(int isWinner, boolean AI) throws IOException {
		String[] command = new String[3];
		if (isWinner == 1) {
			if(AI) {
				command = generateCommand("true" , "true");
			}else {
				command = generateCommand("true" , "false");
			}
		} else if (isWinner == -1) {
			if(AI) {
				command = generateCommand("false" , "true");
			}else {
				command = generateCommand("false" , "false");
			}
		}
		Main.getObjOut().writeObject(command);
		Main.getObjOut().flush();
	}
	private static String[] generateCommand(String isWinner, String AI) {
		String[] command = new String[3];
		command[0] = "gameFinished";
		command[1] = isWinner;
		command[2] = AI;
		return command;
	}
	//---------------------------------AI related methods -------------------------------//
	private class bestMove{ 
	    int bestI, bestJ; 
	}; 
	  
	private int evaluateScore(TicTacToeContent[][] board){ 
		
		// checking for wins or losses
		if(board[0][0] == board[0][1] && board[0][0] == board[0][2] && board[0][0] != null) {
			if (board[0][0] == computer) return +10;
			else if (board[0][0] == player) return -10;
		}
		else if (board[1][0] == board[1][1] && board[1][0] == board[1][2] && board[1][0] != null) {
			if (board[1][0] == computer) return +10;
			else if (board[1][0] == player) return -10;
		}
		else if (board[2][0] == board[2][1] && board[2][0] == board[2][2] && board[2][0] != null) {
			if (board[2][0] == computer) return +10;
			else if (board[2][0] == player) return -10;
		}
		else if (board[0][0] == board[1][0] && board[0][0] == board[2][0] && board[0][0] != null) {
			if (board[0][0] == computer) return +10;
			else if (board[0][0] == player) return -10;
		}
		else if (board[0][1] == board[1][1] && board[0][1] == board[2][1] && board[0][1] != null) {
			if (board[0][1] == computer) return +10;
			else if (board[0][1] == player) return -10;
		}
		else if (board[0][2] == board[1][2] && board[0][2] == board[2][2] && board[0][2] != null) {
			if (board[0][2] == computer) return +10;
			else if (board[0][2] == player) return -10;
		}
		else if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != null) {
			if (board[0][0] == computer) return +10;
			else if (board[0][0] == player) return -10;
		}
		else if (board[2][0] == board[1][1] && board[2][0] == board[0][2] && board[2][0] != null){
			if (board[2][0] == computer) return +10;
			else if (board[2][0] == player) return -10;
		}
		
		// else there's no winner yet!
		return 0; 
	} 
	  
	// this mehod goes through all possible boards
	// returns +10 if the computer is the winner
	// -10 if computer looses and 0 if it's a tie
	
	int minimax(TicTacToeContent[][] board, int depth, Boolean isMax){
		
	    int score = evaluateScore(board); 
	   
	    // if the maximizer is winner (computer)
	    if (score == 10) return score; 
	  
	    // if Minimizer is winner (player)
	    if (score == -10) return score; 
	  
	    // if there's a tie
	    if (isBoardFull()) return 0; 
	  
	    // maximizer's move ( computer)
	    if (isMax){ 
	        int best = -1000; 
	        
	        for (int i = 0; i < 3; i++) { 
	            for (int j = 0; j < 3; j++) { 
	                
	                if (board[i][j]== null) { 
	                    board[i][j] = computer;
	                    
	                    //calls players move
	                    best = Math.max(best, minimax(board, depth + 1, !isMax)); 
	                    // Undo the move 
	                    board[i][j] = null; 
	                } 
	            } 
	        } 
	        return best; 
	    } 
	  
	    // minimizer's move (player)
	    else { 
	        int best = 1000; 
	  
	        for (int i = 0; i < 3; i++) { 
	            for (int j = 0; j < 3; j++) { 
	                
	                if (board[i][j] == null) { 
	                    // Make the move 
	                    board[i][j] = player; 
	  
	                    //calls computer move
	                    best = Math.min(best, minimax(board,  
	                                    depth + 1, !isMax)); 
	  
	                    // Undo the move 
	                    board[i][j] = null; 
	                } 
	            } 
	        } 
	        return best; 
	    } 
	} 
	
	// This will return the best possible 
	// move for the player 
	bestMove findBestMove(TicTacToeContent[][] board) { 
		
	    int bestVal = -10000; 
	    bestMove bestMove = new bestMove(); 
	    bestMove.bestI = -1; 
	    bestMove.bestJ = -1; 
	  
	    
	    for (int i = 0; i < 3; i++) { 
	        for (int j = 0; j < 3; j++) { 
	            if (board[i][j] == null) { 
	            	
	                board[i][j] = computer; 
	  
	                int moveVal = minimax(board, 0, false); 
	  
	                // Undo the move 
	                board[i][j] = null; 
	  
	                if (moveVal > bestVal) { 
	                    bestMove.bestI = i; 
	                    bestMove.bestJ = j; 
	                    bestVal = moveVal; 
	                } 
	            } 
	        } 
	    }
	    return bestMove; 
	}
}
