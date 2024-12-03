// Controller.java
import java.util.Scanner;

public class Controller {
    private final Model model;
    private final View view;
    private final Scanner scanner;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        view.displayMessage("Willkommen bei Vier Gewinnt!");
        view.displayMessage("Spieler 1, bitte geben Sie Ihren Namen ein:");
        String player1 = scanner.nextLine();
        view.displayMessage("Spieler 2, bitte geben Sie Ihren Namen ein:");
        String player2 = scanner.nextLine();

        char[] tokens = {'o', 'x'};
        String[] players = {player1, player2};
        int currentPlayer = 0;

        while (true) {
            view.displayBoard(model.getBoard());
            view.displayMessage(players[currentPlayer] + " (" + tokens[currentPlayer] + "), bitte Spalte wählen (0-6):");

            int column;
            while (true) {
                try {
                    column = Integer.parseInt(scanner.nextLine());
                    if (column < 0 || column >= 7 || model.isColumnFull(column)) {
                        view.displayMessage("Ungültige Spalte. Bitte erneut versuchen:");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    view.displayMessage("Ungültige Eingabe. Bitte eine Zahl zwischen 0 und 6 eingeben:");
                }
            }

            model.dropToken(column, tokens[currentPlayer]);

            char winner = model.checkWinner();
            if (winner != '.') {
                view.displayBoard(model.getBoard());
                view.displayMessage("Herzlichen Glückwunsch, " + players[currentPlayer] + " Sie haben gewonnen!");
                break;
            } else if (model.isBoardFull()) {
                view.displayBoard(model.getBoard());
                view.displayMessage("Das Spiel endet unentschieden!");
                break;
            }

            currentPlayer = 1 - currentPlayer; // Wechsel zwischen 0 und 1
        }
    }
}
