package com.dormire.trading.gui.controllers;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.gui.GUIManager;
import com.dormire.trading.gui.instruments.Instrument;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PromptController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label instrumentLabel;

    @FXML
    private JFXComboBox<String> instrumentChooser;

    @FXML
    private JFXTextField stonkPrice;

    @FXML
    private JFXTextField noStonks;

    @FXML
    private JFXTextField profit;

    @FXML
    private JFXTextField bufferZone;

    @FXML
    private JFXTextField visualBufferZone;

    @FXML
    private JFXTextField waitTime;

    @FXML
    private JFXTextField loopTime;

    @FXML
    private JFXButton okButton;

    @FXML
    private JFXButton cancelButton;

    private MainController mainController;

    private GUIManager guiManager;

    private Instrument instrument;

    public PromptController() {
        this.mainController = MainController.getInstance();
        this.guiManager = GUIManager.getInstance();
    }

    public PromptController(Instrument instrument) {
        this();
        this.instrument = instrument;
    }

    public void initialize() {
        if (instrument == null) {
            createPrompt();

        } else {
            updatePrompt();
        }
    }

    private void createPrompt() {
        instrumentChooser.getItems().addAll("AAPL", "TSLA", "GOOG", "NVDA", "ZM");
        instrumentChooser.getSelectionModel().selectFirst();

        okButton.setOnAction(event -> {
            String ticker = instrumentChooser.getSelectionModel().getSelectedItem();

            if (mainController.getInstrumentManager().contains(ticker)) {
                guiManager.showOkAlert("You have already added " + ticker);
                return;
            }

            Stage stage = getStage(event);
            stage.close();

            mainController.addInstrument(new Instrument(
                    ticker,
                    stonkPrice.getText(),
                    noStonks.getText(),
                    profit.getText(),
                    loopTime.getText(),
                    waitTime.getText(),
                    bufferZone.getText(),
                    visualBufferZone.getText()
            ));
        });

        cancelButton.setOnAction(event -> {
            Stage stage = getStage(event);
            stage.close();
        });
    }

    private void updatePrompt() {
        gridPane.getChildren().remove(instrumentLabel);
        gridPane.getChildren().remove(instrumentChooser);

        updatePromptFields();

        okButton.setText("SAVE");

        okButton.setOnAction(event -> {
            Stage stage = getStage(event);
            stage.close();

            updateInstrumentFields();

            StonkTrader trader = instrument.getTrader();
            trader.update(instrument);
        });

        cancelButton.setOnAction(event -> {
            Stage stage = getStage(event);
            stage.close();
        });
    }

    private void updatePromptFields() {
        stonkPrice.setText(String.valueOf(instrument.getPrice()));
        noStonks.setText(String.valueOf(instrument.getNoStonks()));
        profit.setText(String.valueOf(instrument.getProfit()));
        loopTime.setText(String.valueOf(instrument.getLoopTime()));
        waitTime.setText(String.valueOf(instrument.getWaitTime()));
        bufferZone.setText(String.valueOf(instrument.getBufferZone()));
        visualBufferZone.setText(String.valueOf(instrument.getVisualBufferZone()));
    }

    private void updateInstrumentFields() {
        instrument.setPrice(stonkPrice.getText());
        instrument.setNoStonks(noStonks.getText());
        instrument.setProfit(profit.getText());
        instrument.setLoopTime(loopTime.getText());
        instrument.setWaitTime(waitTime.getText());
        instrument.setBufferZone(bufferZone.getText());
        instrument.setVisualBufferZone(visualBufferZone.getText());
    }

    private Stage getStage(ActionEvent event) {
        return (Stage) ((Node) (event.getSource())).getScene().getWindow();
    }
}