package org.example.viergewinnt;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class View {
    private final GridPane boardGrid;
    private final Button[] columnButtons;
    private final Label messageLabel;
    private Controller controller;

    public View() {
        boardGrid = new GridPane();
        columnButtons = new Button[7];
        messageLabel = new Label();
    }

    // Setter für den Controller hinzufügen
    public void setController(Controller controller) {
        this.controller = controller;
    }

    // Setup UI und Button-Handler
    public void setupUI(Stage stage, Controller controller) {
        this.controller = controller; // Setze den Controller

        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        // Erstelle die 6 Reihen (das Spielfeld) mit 7 Spalten
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                Button cellButton = new Button();
                cellButton.setPrefSize(60, 60);
                boardGrid.add(cellButton, col, row);
            }
        }

        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane mainLayout = new GridPane();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.add(boardGrid, 0, 0);
        mainLayout.add(messageLabel, 0, 1);

        Scene scene = new Scene(mainLayout, 500, 600);
        stage.setTitle("Vier Gewinnt");
        stage.setScene(scene);

        // KeyEvent-Handler hinzufügen
        scene.setOnKeyPressed(this::handleKeyPress);

        stage.show();
    }

    // Spielfeld aktualisieren
    public void updateBoard(char[][] board) {
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                Button cellButton = (Button) getNodeFromGridPane(boardGrid, col, row);
                if (board[row - 1][col] == 'o') {
                    cellButton.setStyle("-fx-background-color: red;");
                } else if (board[row - 1][col] == 'x') {
                    cellButton.setStyle("-fx-background-color: yellow;");
                } else {
                    cellButton.setStyle("-fx-background-color: lightgray;");
                }
            }
        }
    }

    // Nachricht anzeigen
    public void displayMessage(String message) {
        Platform.runLater(() -> messageLabel.setText(message));
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    // Tastatureingabe verarbeiten
    private void handleKeyPress(KeyEvent event) {
            // Prüfe auf die Numpad-Tasten 1 bis 7
            switch (event.getCode()) {
                case NUMPAD1:
                    controller.onColumnSelected(0);
                    break;
                case NUMPAD2:
                    controller.onColumnSelected(1);
                    break;
                case NUMPAD3:
                    controller.onColumnSelected(2);
                    break;
                case NUMPAD4:
                    controller.onColumnSelected(3);
                    break;
                case NUMPAD5:
                    controller.onColumnSelected(4);
                    break;
                case NUMPAD6:
                    controller.onColumnSelected(5);
                    break;
                case NUMPAD7:
                    controller.onColumnSelected(6);
                    break;
                default:
                    break; // Keine Aktion für andere Tasten
            }
        }
}
