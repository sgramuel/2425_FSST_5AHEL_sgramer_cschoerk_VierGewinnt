package org.example.viergewinnt;

import javafx.application.Platform;

public class Controller {
    private final Model model;
    private final View view;
    private String player1, player2;
    private int currentPlayer;
    private final char[] tokens = {'o', 'x'};
    private final String[] players;
    private boolean gameFinished;  // Spielstatus: Ist das Spiel beendet?

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.players = new String[2];
        this.currentPlayer = 0;
        this.gameFinished = false;  // Zu Beginn ist das Spiel nicht beendet
    }

    public void startGame() {
        Platform.runLater(() -> {
            view.displayMessage("Willkommen bei Vier Gewinnt!");
            // Hier Namen der Spieler festlegen
            players[0] = "Spieler 1";
            players[1] = "Spieler 2";
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), Ihre Runde!");

            // Setze die Benutzeroberfläche auf
            view.setupUI(new javafx.stage.Stage(), this);
        });
    }

    public void onColumnSelected(int column) {
        if (gameFinished) {  // Wenn das Spiel bereits beendet ist, keine weiteren Züge
            return;
        }

        if (model.isColumnFull(column)) {
            view.displayMessage("Diese Spalte ist voll. Bitte wählen Sie eine andere.");
            return;
        }

        model.dropToken(column, tokens[currentPlayer]);
        view.updateBoard(model.getBoard());

        char winner = model.checkWinner();
        if (winner != '.') {
            view.displayMessage("Herzlichen Glückwunsch, " + players[currentPlayer] + " Sie haben gewonnen!");
            gameFinished = true;  // Spiel ist beendet
        } else if (model.isBoardFull()) {
            view.displayMessage("Das Spiel endet unentschieden!");
            gameFinished = true;  // Spiel ist beendet
        } else {
            currentPlayer = 1 - currentPlayer; // Wechsel zwischen 0 und 1
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), Ihre Runde!");
        }
    }
}