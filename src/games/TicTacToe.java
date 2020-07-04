package games;

public class TicTacToe {
	private TicTacToeContent[][] board;
	private TicTacToeContent computer = TicTacToeContent.O,opponent = TicTacToeContent.O, player = TicTacToeContent.X;
	private boolean EOGame = false;
	private boolean playerWin = false;
	private boolean AI = true;
	private int winner = 0; // if the current player is the winner this variable will be 1
	private int lastOpponentI, lastOpponentJ;
	
	
	public TicTacToe() {
		board = new TicTacToeContent[3][3];
		EOGame = false;
	}
	
	
	public void oponentTurn() {
		
		if (AI) {
			bestMove bestMove = findBestMove(board); 
			  
			lastOpponentI = bestMove.bestI;
			lastOpponentJ = bestMove.bestJ;
			
			if(lastOpponentI != -1) board[lastOpponentI][lastOpponentJ] = TicTacToeContent.O;
			
			if (checkForWin()) {
				winner = -1;
				System.out.println("Game over!");
			}
		} else {
			
		}
		
	}
	
	
	public boolean setOnBoard(int curI, int curJ, TicTacToeContent content) {
		if (isValid(curI, curJ) && !EOGame) {
			
			board[curI][curJ] = content;
			if (checkForWin()) {
				System.out.println("U won!");
				playerWin = true;
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
	public int getLastOpponentJ() {
		return lastOpponentJ;
	}
	public int getLastOpponentI() {
		return lastOpponentI;
	}
	public int getWinner() {
		return winner;
	}
	public void setAI(boolean aI) {
		AI = aI;
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
