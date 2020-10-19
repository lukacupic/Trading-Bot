package com.dormire.trading.gui.controllers;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.gui.instruments.Instrument;
import com.dormire.trading.gui.instruments.InstrumentManager;
import com.dormire.trading.gui.scenes.PromptScene;
import com.dormire.trading.gui.GuiManager;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;

public class MainController {

    private static final String TICKER_LOGO_URL = "https://trading212equities.s3.eu-central-1.amazonaws.com/%s.png";

    private static MainController instance;

    @FXML
    private JFXButton addButton;

    @FXML
    private VBox instrumentSidebar;

    @FXML
    private Label stepLabel;

    @FXML
    private Label mainLabel;

    @FXML
    private ImageView ringView;

    private GuiManager guiManager;
    private InstrumentManager instrumentManager;
    private StonkDriverManager driverManager;

    public MainController() {
        MainController.instance = this;
    }

    public void initialize() {
        this.driverManager = new StonkDriverManager();
        this.guiManager = new GuiManager(ringView, mainLabel, stepLabel);
        this.instrumentManager = new InstrumentManager(instrumentSidebar);

        driverManager.start();

        createHomeBox();

        addButton.setOnAction(event -> {
            try {
                Scene mainScene = ((Node) (event.getSource())).getScene();
                new PromptScene(mainScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        double ringSize = 350;
        ringView.setFitWidth(ringSize);
        ringView.setFitHeight(ringSize);

        bindLabelFont(stepLabel, 0.8 * ringSize, 18);
        bindLabelFont(mainLabel, 0.8 * ringSize, 18);
    }

    private void createHomeBox() {
        HBox homeBox = loadInstrument();
        Instrument homeInstrument = new Instrument(homeBox);

        homeBox.setOnMouseClicked(event -> {
            instrumentManager.setActive(homeInstrument);
            guiManager.updateActiveTrader(null);
            guiManager.updateStep(0);
            guiManager.updateMessage("");
        });
        instrumentManager.addInstrument(homeInstrument);

        ImageView imageView = (ImageView) homeBox.getChildren().get(0);
        InputStream homeStream = getClass().getResourceAsStream("/images/home.png");
        imageView.setImage(new Image(homeStream));
        imageView.setFitWidth(50);
    }

    public void addInstrument(Instrument instrument) {
        StonkTrader trader = new StonkTrader(guiManager, driverManager, instrument);
        trader.start();
        ringView.setVisible(true);

        createInstrumentBox(instrument, trader);
    }

    private void createInstrumentBox(Instrument instrument, StonkTrader trader) {
        HBox instrumentBox = loadInstrument();

        // instrument logo
        new Thread(() -> {
            String url = String.format(TICKER_LOGO_URL, instrument.getTicker());
            ImageView imageView = (ImageView) instrumentBox.getChildren().get(0);
            imageView.setImage(new Image(url));
        }).start();

        // mouse handling
        instrumentBox.setOnMouseClicked(event -> {
            instrumentManager.setActive(instrument);
            guiManager.updateActiveTrader(trader);
            guiManager.refresh();
        });

        // context menu
        ContextMenu contextMenu = createContextMenu(instrument, trader);
        instrumentBox.setOnContextMenuRequested(event -> {
            contextMenu.show(instrumentBox, event.getScreenX(), event.getScreenY());
        });

        instrument.setBox(instrumentBox);

        instrumentManager.addInstrument(instrument);

        // TODO: remove instrument mouse-clicked duplicate by delegating the task to the instrument itself
        instrumentManager.setActive(instrument);
        guiManager.updateActiveTrader(trader);
        guiManager.refresh();
    }

    private ContextMenu createContextMenu(Instrument instrument, StonkTrader trader) {
        ContextMenu menu = new ContextMenu();

        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(event -> {
//            instrumentManager.edit(instrument);
        });
        MenuItem item2 = new MenuItem("Remove");
        item2.setOnAction(event -> {
            instrumentManager.removeInstrument(instrument);
            trader.interrupt();
        });

        menu.getItems().addAll(item1, item2);

        return menu;
    }

    private HBox loadInstrument() {
        try {
            return FXMLLoader.load(getClass().getResource("/interfaces/instrument.fxml"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void bindLabelFont(Label label, double ringSize, double defaultFontSize) {
        Font defaultFont = Font.font(defaultFontSize);

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tempText = new Text(newValue);
            tempText.setFont(defaultFont);

            double textWidth = tempText.getLayoutBounds().getWidth();

            if (textWidth <= ringSize) {
                label.setFont(defaultFont);

            } else {
                double newFontSize = defaultFontSize * ringSize / textWidth;
                label.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
    }

    public void shutdown() {
        driverManager.dispose();
    }

    public InstrumentManager getInstrumentManager() {
        return instrumentManager;
    }

    public static MainController getInstance() {
        return instance;
    }
}