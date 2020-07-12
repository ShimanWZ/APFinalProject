package application;

import java.io.IOException;

import games.TicTacToe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class GameEndedController {
	@FXML private Label gameStatus;
	@FXML private Label compWins;
	@FXML private Label comLosses;
	@FXML private Label playerWins;
	@FXML private Label playerLosses;
	private static TicTacToe game;
	
	@FXML private void initializeScene() {
		if (game.getWinner() == 1) {
			gameStatus.setText("Yay! You Won!");
		} else if (game.getWinner() == 0) {
			gameStatus.setText("There was a tie!");
		}else {
			gameStatus.setText("Ooops! You Lost!");
		}
		
		compWins.setText("Wins from computer: " + Main.getCurUser().getTictactoeCompWins());
		comLosses.setText("Losses from computer: " + Main.getCurUser().getTictactoeCompLosses());
		playerWins.setText("Wins from players: " + Main.getCurUser().getTictactoePlayWins());
		playerLosses.setText("Losses from players: " + Main.getCurUser().getTictactoePlayLosses());
			
	}
	@FXML private void closeWindow() {
		Main.getGameStage().close();
	}
	@FXML private void startANewGame() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("tictactoe.fxml"));
		Main.tictactoe = new Scene(root);
		Main.tictactoe.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Main.getGameStage().setScene(Main.tictactoe);
	}
	public static void setGame(TicTacToe game) {
		GameEndedController.game = game;
	}
}
