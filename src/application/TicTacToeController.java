package application;

import java.io.IOException;
import java.util.ArrayList;

import base.OpponentMoveListener;
import fileHandling.WriteFile;
import games.GamesUtils;
import games.TicTacToe;
import games.TicTacToeContent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TicTacToeController {
	@FXML private AnchorPane pane;
	@FXML private Label winsCount;
	@FXML private Label lossCount;
	@FXML private Label turn;
	private TicTacToe curGame = new TicTacToe();
	private ArrayList<Object> boardContents = new ArrayList<Object>(); //this arraylist comes in handy when we want to clear the screen
	public static boolean propertiesRecieved;
	
	
	{
		Main.getCurUser().setOpponentMoveListener(new OpponentMoveListener() {
			@Override
			public void onMoveMade(int i, int j) {
				curGame.setOpponentOnBord(i, j);
				Platform.runLater(new Runnable() {
			            @Override public void run() {
							draw(i, j, 'O');
							turn.setText("your turn");
			            }
			    });
				curGame.setMyTurn(true);

				try {
					if (curGame.getIfOpponentWon() || curGame.getWinner() == 0) {
						WriteFile.TicTacToeFile.writeWinsAndLosses(curGame);
						Platform.runLater(new Runnable() {
					        @Override public void run() {
								try {
									reset();
									setToGameEndedScene();
								} catch (IOException e) {e.printStackTrace();}
					        }
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	@FXML
	private void clicked(MouseEvent event) throws IOException {
		double curX = event.getSceneX(), curY = event.getSceneY();
		int curI = GamesUtils.getTicTacToeI(curY), curJ = GamesUtils.getTicTacToeJ(curX);
		
		//checks if we clicked on the board
		if (curI != -1 && curJ != -1 && curGame.isMyTurn()) {
			//if it is placed, the computer turn will be called
			if(curGame.setOnBoard(curI, curJ, TicTacToeContent.X)) {
				draw(curI, curJ, 'X');

				curGame.setMyTurn(false);
				turn.setText("");
				
				if (Main.getIsGameWithAI()) {
					curGame.oponentTurn();
					draw(curGame.getLastOpponentI(), curGame.getLastOpponentJ(), 'O');
					curGame.setMyTurn(true);
					turn.setText("your turn");
					propertiesRecieved = false;
				}
			}
		}
			
		if (curGame.endOfGame()) {
			WriteFile.TicTacToeFile.writeWinsAndLosses(curGame);
			reset();
			setToGameEndedScene();
		}
	}
	
	@FXML
	private void reset() {
		if (Main.getIsGameWithAI()) {
			for (Object o : boardContents) {
				pane.getChildren().remove(o);
			}
			boardContents.clear();
			curGame = new TicTacToe();
		}
	}
	
	private void draw(int curI, int curJ, char turn) {
		
		
		winsCount.setText((Main.getCurUser().getTictactoeCompWins() + Main.getCurUser().getTictactoePlayWins()) + "");
		lossCount.setText((Main.getCurUser().getTictactoeCompLosses() + Main.getCurUser().getTictactoePlayLosses())+ "");
		
		
		
		
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
		l1.setFill(Color.web("#101820"));
		l1.setStroke(Color.web("#101820"));
		l1.setStrokeWidth(5);
		
		//initializing line2
		Line l2 = new Line(curX, curY + 80, curX + 80, curY);
		l2.setFill(Color.web("#101820"));
		l2.setStroke(Color.web("#101820"));
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
		o.setStroke(Color.web("#101820"));
		o.setStrokeWidth(5);
		
		//adding circle to anchorpane and arraylist of objects
		pane.getChildren().add(o);
		boardContents.add(o);
	}
	private void setToGameEndedScene() throws IOException {
		GameEndedController.setGame(curGame);
		Parent root = FXMLLoader.load(getClass().getResource("GameEndedScene.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.getGameStage().setScene(Main.tictactoe);
	}
	public TicTacToe getCurrGame() {
		return curGame;
	}
	public void setCurrGame(TicTacToe currGame) {
		this.curGame = currGame;
	}
}
