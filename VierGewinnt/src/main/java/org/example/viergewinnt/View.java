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
    private final Label messageLabel;
    private Controller controller;

    public View() {
        boardGrid = new GridPane();
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

        for (int i = 0; i < 7; i++) {
            Label columnLabel = new Label(String.valueOf(i + 1)); // 1 bis 7
            columnLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Erstelle ein HBox, um den Label-Inhalt zu zentrieren
            javafx.scene.layout.HBox hBox = new javafx.scene.layout.HBox(columnLabel);
            hBox.setAlignment(Pos.CENTER);  // Zentriert den Inhalt in der HBox

            boardGrid.add(hBox, i, 0);  // Platziere die HBox oben auf das Spielfeld
        }

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
    public void displayJokerField(int column) {
        Button jokerCell = (Button) getNodeFromGridPane(boardGrid, column, 1);
        jokerCell.setStyle("-fx-background-color: green;");  // Hier grün für das Joker-Feld
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
        // Prüfe auf die Zahlentasten 1 bis 7
        switch (event.getCode()) {
            case DIGIT1:
                controller.onColumnSelected(0);
                break;
            case DIGIT2:
                controller.onColumnSelected(1);
                break;
            case DIGIT3:
                controller.onColumnSelected(2);
                break;
            case DIGIT4:
                controller.onColumnSelected(3);
                break;
            case DIGIT5:
                controller.onColumnSelected(4);
                break;
            case DIGIT6:
                controller.onColumnSelected(5);
                break;
            case DIGIT7:
                controller.onColumnSelected(6);
                break;
            default:
                break; // Keine Aktion für andere Tasten
        }
    }
}
