package application;

import java.util.ArrayList;

import fileHandling.WriteFile;
import games.GamesUtils;
import games.TicTacToe;
import games.TicTacToeContent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TicTacToeController {
	@FXML private AnchorPane pane;
	private TicTacToe curGame = new TicTacToe();
	private ArrayList<Object> boardContents = new ArrayList<Object>(); //this arraylist comes in handy when we want to clear the screen

	
	@FXML
	private void clicked(MouseEvent event) {
		double curX = event.getSceneX(), curY = event.getSceneY();
		int curI = GamesUtils.getTicTacToeI(curY), curJ = GamesUtils.getTicTacToeJ(curX);
		
		//checks if we clicked on the board
		if (curI != -1 && curJ != -1) {
			//if it is placed, the computer turn will be called
			if(curGame.setOnBoard(curI, curJ, TicTacToeContent.X)) {
				draw(curI, curJ, 'X');
				curGame.computerTurn();
				draw(curGame.getLastComputerI(), curGame.getLastComputerJ(), 'O');
			}
		}
		
		if (curGame.endOfGame()) {
			WriteFile.TicTacToeFile.writeWinsAndLosses(curGame);
			boardContents.clear();
			System.exit(0);
		}
	}
	@FXML
	private void reset() {
		for (Object o : boardContents) {
			pane.getChildren().remove(o);
		}
		boardContents.clear();
		curGame = new TicTacToe();
	}
	
	private void draw(int curI, int curJ, char turn) {
		int curX = curJ * 100 + 35;
		int curY = curI * 100 + 35;
		
		if (turn == 'X') {
			drawX(curX, curY);
		}
		if (turn == 'O') {
			drawO(curX, curY);
		}
	}
	private void drawX(int curX, int curY) {
		// initializing line1
		Line l1 = new Line(curX, curY, curX + 80, curY + 80);
		l1.setFill(Color.web("#0d3e51"));
		l1.setStroke(Color.web("#0d3e51"));
		l1.setStrokeWidth(5);
		
		//initializing line2
		Line l2 = new Line(curX, curY + 80, curX + 80, curY);
		l2.setFill(Color.web("#0d3e51"));
		l2.setStroke(Color.web("#0d3e51"));
		l2.setStrokeWidth(5);
		
		//adding lines to our anchorpane
		pane.getChildren().add(l1);
		pane.getChildren().add(l2);
		
		//adding lines to our arraylist of objects
		boardContents.add(l2);
		boardContents.add(l1);
	}
	private void drawO(int curX, int curY) {
		//initializing the circle
		Circle o = new Circle();
		o.setCenterX(curX + 40);
		o.setCenterY(curY + 40);
		o.setRadius(35);
		o.setFill(null);
		o.setStroke(Color.web("#0d3e51"));
		o.setStrokeWidth(5);
		
		//adding circle to anchorpane and arraylist of objects
		pane.getChildren().add(o);
		boardContents.add(o);
	}
	
	
	public TicTacToe getCurrGame() {
		return curGame;
	}
	public void setCurrGame(TicTacToe currGame) {
		this.curGame = currGame;
	}
}
