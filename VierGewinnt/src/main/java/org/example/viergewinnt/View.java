package org.example.viergewinnt;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class View {
    private final GridPane boardGrid;
    private final Label messageLabel;
    private Controller controller;
    private ComboBox<String> gameModeComboBox;

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

        // Spaltenbeschriftungen hinzufügen
        for (int i = 0; i < 7; i++) {
            Label columnLabel = new Label(String.valueOf(i + 1)); // 1 bis 7
            columnLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Erstelle ein HBox, um den Label-Inhalt zu zentrieren
            javafx.scene.layout.HBox hBox = new javafx.scene.layout.HBox(columnLabel);
            hBox.setAlignment(Pos.CENTER); // Zentriert den Inhalt in der HBox

            boardGrid.add(hBox, i, 0); // Platziere die HBox oben auf das Spielfeld
        }

        // Spielfeld erstellen (6 Reihen x 7 Spalten)
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                Button cellButton = new Button();
                cellButton.setPrefSize(60, 60);
                boardGrid.add(cellButton, col, row);
            }
        }

        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Infobutton erstellen
        Button infoButton = new Button("Info");
        infoButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        infoButton.setOnAction(event -> showInfoPopup());

        // Spielmodus-ComboBox erstellen
        gameModeComboBox = new ComboBox<>();
        gameModeComboBox.getItems().addAll("Best of 1", "Best of 3", "Best of 5");
        gameModeComboBox.setValue("Best of 1"); // Standardwert
        gameModeComboBox.setOnAction(event -> {
            String selectedMode = gameModeComboBox.getValue();
            controller.setGameMode(selectedMode); // Setze den Spielmodus im Controller
        });

        // Oberes Layout für InfoButton und ComboBox
        GridPane topBar = new GridPane();
        topBar.setHgap(10);
        topBar.add(gameModeComboBox, 0, 0); // ComboBox links
        topBar.add(infoButton, 1, 0);      // Infobutton rechts
        topBar.setAlignment(Pos.TOP_RIGHT);

        // Refresh-Button erstellen
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        refreshButton.setOnAction(event -> controller.resetGame());

        // Unteres Layout für Refresh-Button
        GridPane bottomBar = new GridPane();
        bottomBar.add(refreshButton, 0, 0); // Refresh-Button zur unteren Leiste hinzufügen
        bottomBar.setAlignment(Pos.CENTER); // Zentriere den Button

        // Hauptlayout
        GridPane mainLayout = new GridPane();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.add(topBar, 0, 0);         // Oberes Layout hinzufügen
        mainLayout.add(boardGrid, 0, 1);      // Spielfeld darunter
        mainLayout.add(messageLabel, 0, 2);   // Nachrichtenfeld darunter
        mainLayout.add(bottomBar, 0, 3);      // Unteres Layout mit Refresh-Button hinzufügen

        Scene scene = new Scene(mainLayout, 500, 600);
        stage.setTitle("Vier Gewinnt");
        stage.setScene(scene);

        // KeyEvent-Handler hinzufügen
        scene.setOnKeyPressed(this::handleKeyPress);
        stage.show();
    }



    private void showInfoPopup() {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Vier Gewinnt - Spielregeln");
        infoAlert.setHeaderText("Spielregeln");
        infoAlert.setContentText(
                "1. Spieler 1 (Rot) und Spieler 2 (Gelb) spielen abwechselnd.\n" +
                "2. Wer zuerst, 4 gleiche Spielsteine in einer Reihe - Horizontal, Vertikal\n" +
                    "oder Diagonal hat, gewinnt.\n" +
                "3. Das Spiel endet unentschieden, wenn das Spielfeld voll ist.\n" +
                "4. Bei plazieren eines Spielsteins auf ein Spezialfeld (Grün) erhält der\n" +
                    "jeweilige Spieler einen von drei möglichen Joker-Zügen:.\n" +
                        "- der Letzte Spielzug kann Rückgängig gemacht werden\n" +
                        "- ein Spielstein kann teleportiert werden\n\n" +
                "Viel Spaß beim Spielen!"
        );
        infoAlert.showAndWait();
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
