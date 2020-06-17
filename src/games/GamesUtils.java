package games;

public class GamesUtils {
	
	//--------------------- tic tac toe utils-----------------------//
	public static int getTicTacToeI(double y) {
		if (y >= 25 && y < 125) return 0;
		else if(y >= 125 && y <= 225) return 1;
		else if(y >= 225 && y < 325) return 2;
		return -1;
	}
	public static int getTicTacToeJ(double curGame) {
		if (curGame >= 25 && curGame < 125) return 0;
		else if(curGame >= 125 && curGame <= 225) return 1;
		else if(curGame >= 225 && curGame < 325) return 2;
		return -1;
	}
	
	
}
