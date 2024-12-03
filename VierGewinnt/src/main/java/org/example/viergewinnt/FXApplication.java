package org.example.viergewinnt;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view);

        controller.startGame();

    }

    public static void main(String[] args) {
        launch();
    }


}