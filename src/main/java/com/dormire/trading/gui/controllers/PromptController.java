package com.dormire.trading.gui.controllers;

import com.dormire.trading.gui.instruments.Instrument;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

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
    private JFXButton okButton;

    @FXML
    private JFXButton cancelButton;

    private MainController mainController;

    public PromptController() {
        ControllerManager.getInstance().setPromptController(this);
        this.mainController = ControllerManager.getInstance().getMainController();

    }

    public void initialize() {
        instrumentBox.getItems().addAll("AAPL", "TSLA", "GOOG", "NVDA", "ZM");
        instrumentBox.getSelectionModel().selectFirst();

        okButton.setOnAction(event -> {
            String ticker = instrumentBox.getSelectionModel().getSelectedItem();

            if (mainController.getInstrumentManager().contains(ticker)) {
                mainController.showAlert("You have already added " + ticker);
                return;
            }

            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.close();

            Instrument instrument = new Instrument(
                    ticker,
                    stonkPrice.getText(),
                    noStonks.getText(),
                    stonkPercentage.getText());

            mainController.addInstrument(instrument);
        });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            stage.close();
        });
    }
}