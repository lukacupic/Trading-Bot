package com.dormire.trading.gui.scenes;

import com.dormire.trading.gui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainScene extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/main.fxml"));

        MainController controller = new MainController();
        loader.setController(controller);

        Parent root = loader.load();
        stage.setScene(new Scene(root, Color.BLACK));
        stage.setTitle("Stonk Demo v0.0.1");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}