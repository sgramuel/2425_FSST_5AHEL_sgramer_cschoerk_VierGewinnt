import javafx.application.Platform;

public class Controller {
    private final Model model;
    private final View view;
    private String player1, player2;
    private int currentPlayer;
    private final char[] tokens = {'o', 'x'};
    private final String[] players;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.players = new String[2];
        this.currentPlayer = 0;
    }

    public void startGame() {
        Platform.runLater(() -> {
            view.displayMessage("Willkommen bei Vier Gewinnt!");
            // Here you would set player names using a popup or input fields.
            players[0] = "Spieler 1";
            players[1] = "Spieler 2";
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), Ihre Runde!");

            // Set up the view
            view.setupUI(new javafx.stage.Stage());
        });
    }

    public void onColumnSelected(int column) {
        if (model.isColumnFull(column)) {
            view.displayMessage("Diese Spalte ist voll. Bitte wählen Sie eine andere.");
            return;
        }

        model.dropToken(column, tokens[currentPlayer]);
        view.updateBoard(model.getBoard());

        char winner = model.checkWinner();
        if (winner != '.') {
            view.displayMessage("Herzlichen Glückwunsch, " + players[currentPlayer] + " Sie haben gewonnen!");
        } else if (model.isBoardFull()) {
            view.displayMessage("Das Spiel endet unentschieden!");
        } else {
            currentPlayer = 1 - currentPlayer; // Wechsel zwischen 0 und 1
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), Ihre Runde!");
        }
    }
}