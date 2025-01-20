package org.example.viergewinnt;

import javafx.application.Platform;

import java.util.Stack;

public class Controller {
    private final Model model;
    private final View view;
    private String player1, player2;
    private int currentPlayer;
    private final char[] tokens = {'o', 'x'};
    private final String[] players;
    private boolean gameFinished;  // Spielstatus: Ist das Spiel beendet?
    private String gameMode = "Best of 1";  // Standardwert
    private int maxGames;  // Maximale Anzahl an Spielen (1, 3 oder 5)
    private int player1Wins;
    private int player2Wins;
    // Spezialfeld und Joker
    private int moveCount;        // Zähler für die Anzahl der Züge
    private boolean jokerAvailable;  // Hat der Spieler den Joker freigeschaltet?
    private int jokerColumn;      // Spalte, in der das Spezialfeld erscheint

    // Undo-Funktionalität
    private Stack<int[]> moveHistory;  // Stack zur Speicherung der letzten Züge

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.players = new String[2];
        this.currentPlayer = 0;
        this.gameFinished = false;  // Zu Beginn ist das Spiel nicht beendet
        this.moveCount = 0;         // Zähler auf 0 setzen
        this.jokerAvailable = false; // Joker ist noch nicht verfügbar
        this.jokerColumn = -1;       // Keine Joker-Spalte initial
        this.moveHistory = new Stack<>();  // Stack für die Züge
    }

    public void startGame() {
        Platform.runLater(() -> {
            view.displayMessage("Willkommen bei Vier Gewinnt!");

            // Zeige das Popup für die Namenseingabe
            view.showPlayerNamePopup(this);

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

        model.dropToken(column, tokens[currentPlayer]);  // Benutze den Token des aktuellen Spielers
        moveHistory.push(new int[]{column, currentPlayer});  // Speichere den Zug
        view.updateBoard(model.getBoard());

        // Überprüfen, ob es einen Gewinner gibt
        char winner = model.checkWinner();
        if (winner != '.') {
            if (winner == 'o') {
                player1Wins++;
            } else {
                player2Wins++;
            }
            view.displayMessage("Herzlichen Glückwunsch, " + players[currentPlayer] + " Sie haben gewonnen!");
            gameFinished = true;  // Spiel ist beendet

            // Überprüfen, ob ein Spieler das Best-of-X erreicht hat
            if ((gameMode.equals("Best of 1") && player1Wins + player2Wins == 1) ||
                    (gameMode.equals("Best of 3") && player1Wins == 2) ||
                    (gameMode.equals("Best of 5") && player1Wins == 3)) {
                view.displayMessage(players[currentPlayer] + " hat das Spiel gewonnen!");
            }
        } else if (model.isBoardFull()) {
            view.displayMessage("Das Spiel endet unentschieden!");
            gameFinished = true;  // Spiel ist beendet
        } else {
            currentPlayer = 1 - currentPlayer; // Wechsel zwischen 0 und 1
            view.displayMessage(players[currentPlayer] + " ist am Zug!");
        }
    }
    public void setGameMode(String mode) {
        this.gameMode = mode;

        // Setze die maximale Anzahl an Spielen je nach Auswahl
        switch (mode) {
            case "Best of 3":
                maxGames = 3;
                break;
            case "Best of 5":
                maxGames = 5;
                break;
            default:
                maxGames = 1;
                break;
        }

        player1Wins = 0;
        player2Wins = 0;
        gameFinished = false;
        view.displayMessage("Spielmodus: " + gameMode);
    }

    public void setPlayerNames(String player1, String player2) {
        this.players[0] = player1;
        this.players[1] = player2;
        view.displayMessage(players[currentPlayer] + " ist am Zug!");
    }


    public void resetGame() {
        model.resetBoard(); // Modell zurücksetzen
        view.updateBoard(model.getBoard()); // Ansicht aktualisieren
        currentPlayer = 0; // Spieler zurücksetzen
        gameFinished = false; // Spielstatus zurücksetzen
        moveHistory.clear(); // Zug-Historie löschen
        view.displayMessage("Das Spiel wurde zurückgesetzt. " + players[currentPlayer] + " beginnt!");
    }


    // Joker aktiviere
    private void activateJoker() {
        // Zeige dem Spieler die Joker-Optionen: Rückgängig machen, Doppelzug oder Feld ersetzen
        view.displayMessage("Du hast einen Joker! Wähle eine Aktion: Rückgängig, Doppelzug oder Feld ersetzen.");
        // Diese Logik könnte in einer UI-Komponente wie einem Popup umgesetzt werden.
    }

    // Undo-Aktion: Rückgängig machen des letzten Zugs
    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            int[] lastMove = moveHistory.pop();  // Hole den letzten Zug
            int column = lastMove[0];
            int player = lastMove[1];

            // Rückgängig machen des Zugs auf dem Spielfeld
            //model.removeToken(column);
            view.updateBoard(model.getBoard());

            // Wechseln zum anderen Spieler
            currentPlayer = 1 - player;
            view.displayMessage(players[currentPlayer] + " ist am Zug!");
        }
    }

    // Doppelzug-Aktion: Ein zusätzlicher Zug für den aktuellen Spieler
    public void doubleMove() {
        view.displayMessage("Du kannst jetzt einen weiteren Zug machen.");
        // Implementiere hier die Logik, damit der Spieler den Zug erneut machen kann
        // Der Spieler kann jetzt eine weitere Spalte wählen, ohne dass der Gegner dazwischen spielt.
    }

    // Feld ersetzen-Aktion: Ersetze ein Feld durch das Token des aktuellen Spielers
    public void replaceField() {
        view.displayMessage("Wähle ein Feld zum Ersetzen.");
        // Implementiere die Logik zur Auswahl eines Feldes und zum Ersetzen durch das Token des Spielers
        // Hier könnte der Spieler ein Feld auswählen, das dann mit dem Token ersetzt wird.
    }

    // Getter für die Joker-Spalte
    public int getJokerColumn() {
        return jokerColumn;
    }
}
