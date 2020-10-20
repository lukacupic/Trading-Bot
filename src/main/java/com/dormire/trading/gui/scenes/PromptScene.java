package com.dormire.trading.gui.scenes;

import com.dormire.trading.gui.controllers.PromptController;
import com.dormire.trading.gui.instruments.Instrument;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PromptScene {

    public PromptScene(Scene mainScene) {
        this(mainScene, new PromptController());
    }

    public PromptScene(Scene mainScene, Instrument instrument) {
        this(mainScene, new PromptController(instrument));
    }

    private PromptScene(Scene mainScene, PromptController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/prompt.fxml"));
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Instrument Chooser");
            stage.setOnHiding(windowEvent -> mainScene.getRoot().setOpacity(1));
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);

            mainScene.getRoot().setOpacity(0.65);

            // set and wait for the prompt window to open
            stage.initOwner(mainScene.getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}