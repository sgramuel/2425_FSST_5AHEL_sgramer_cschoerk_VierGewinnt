package org.example.viergewinnt;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class View {
    private final GridPane boardGrid;
    private final Button[] columnButtons;
    private final Label messageLabel;

    public View() {
        boardGrid = new GridPane();
        columnButtons = new Button[7];
        messageLabel = new Label();
    }

    public void setupUI(Stage stage) {
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        for (int i = 0; i < 7; i++) {
            columnButtons[i] = new Button("↓");
            final int col = i;
            columnButtons[i].setOnAction(event -> onColumnButtonClick(col));
            boardGrid.add(columnButtons[i], i, 0);  // Buttons for each column at the top
        }

        // Create 6 rows (representing the board) with 7 columns
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
        stage.show();
    }

    public void updateBoard(char[][] board) {
        // Update the grid with the current board state
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

    private void onColumnButtonClick(int column) {
        // This will notify the controller that a column has been selected
    }

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
}