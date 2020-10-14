package com.dormire.trading.gui.controllers;

import com.dormire.trading.gui.instruments.Instrument;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PromptController {

    @FXML
    private JFXComboBox<String> instrumentBox;

    @FXML
    private JFXTextField stonkPrice;

    @FXML
    private JFXTextField noStonks;

    @FXML
    private JFXTextField stonkPercentage;

    @FXML
    private JFXButton closeButton;

    private Scene mainScene;

    public PromptController(Scene mainScene) {
        ControllerManager.getInstance().setPromptController(this);

        this.mainScene = mainScene;
    }

    public void initialize() {
        instrumentBox.getItems().addAll("AAPL", "TSLA", "GOOG", "NVDA", "ZM");
        instrumentBox.getSelectionModel().selectFirst();

        closeButton.setOnAction(event -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            Stage stage = (Stage) window;
            stage.close();

            MainController mainController = ControllerManager.getInstance().getMainController();

            Instrument instrument = new Instrument(
                    instrumentBox.getSelectionModel().getSelectedItem(),
                    stonkPrice.getText(),
                    noStonks.getText(),
                    stonkPercentage.getText());

            mainController.addInstrument(instrument);

            mainScene.getRoot().setEffect(null);
        });
    }
}