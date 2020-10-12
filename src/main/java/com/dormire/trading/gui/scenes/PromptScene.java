package com.dormire.trading.gui.scenes;

import com.dormire.trading.gui.controllers.PromptController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PromptScene {

    private static Effect blur = new GaussianBlur(15);

    public PromptScene(Scene mainScene) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/prompt.fxml"));

        PromptController controller = new PromptController(mainScene);
        loader.setController(controller);

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Instrument Prompt");
        stage.setOnHiding(windowEvent -> {
            mainScene.getRoot().setEffect(null);
        });
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);

        // set the blur for the main screen
        mainScene.getRoot().setEffect(blur);

        // set and wait for the prompt window to open
        stage.initOwner(mainScene.getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}