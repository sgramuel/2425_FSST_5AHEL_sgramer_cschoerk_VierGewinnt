package org.example.viergewinnt;

import javafx.application.Application;
import javafx.stage.Stage;


public class VierGewinntApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view);

        view.setController(controller);  // Controller in View setzen
        controller.startGame();
    }
}