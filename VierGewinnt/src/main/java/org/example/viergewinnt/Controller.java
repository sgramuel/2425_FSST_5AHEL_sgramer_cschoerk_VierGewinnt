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

        // Wenn das Spezialfeld verfügbar ist und der Spieler darauf klickt, aktiviere den Joker
        if (jokerAvailable && column == jokerColumn) {
            activateJoker();
            return;
        }

        if (model.isColumnFull(column)) {
            view.displayMessage("Diese Spalte ist voll. Bitte wählen Sie eine andere.");
            return;
        }

        // Token in die gewählte Spalte setzen
        model.dropToken(column, tokens[currentPlayer]);
        moveHistory.push(new int[]{column, currentPlayer});  // Speichere den Zug
        view.updateBoard(model.getBoard());

        // Zähler für Züge erhöhen
        moveCount++;

        // Überprüfen, ob das Spezialfeld erscheinen soll
        if (moveCount >= 10 && !jokerAvailable) {  // Beispiel: nach 10 Zügen erscheint das Spezialfeld
            jokerColumn = column;  // Setze das Spezialfeld in die ausgewählte Spalte
            jokerAvailable = true;
            view.displayJokerField(jokerColumn);  // Joker-Feld anzeigen
        }

        // Überprüfe, ob es einen Gewinner gibt
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

    // Joker aktivieren
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
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), Ihre Runde!");
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
