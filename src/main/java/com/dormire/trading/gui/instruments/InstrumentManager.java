package com.dormire.trading.gui.instruments;

import com.dormire.trading.gui.GuiManager;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class InstrumentManager {

    private List<HBox> instruments;
    private VBox instrumentPane;
    private GuiManager guiManager;

    private Color focusColor = Color.rgb(31, 31, 31);
    private Color unfocusColor = Color.rgb(45, 44, 45);

    private Background focusBackground;
    private Background unfocusBackground;

    public InstrumentManager(GuiManager guiManager, VBox instrumentPane) {
        this.instruments = new LinkedList<>();
        this.guiManager = guiManager;
        this.instrumentPane = instrumentPane;
        this.focusBackground = new Background(new BackgroundFill(focusColor, CornerRadii.EMPTY, Insets.EMPTY));
        this.unfocusBackground = new Background(new BackgroundFill(unfocusColor, CornerRadii.EMPTY, Insets.EMPTY));
    }

    public void addInstrument(HBox instrument) {
        instruments.add(instrument);
        instrumentPane.getChildren().add(instrument);
    }

    public synchronized void setActive(HBox instrument) {
        for (HBox current : instruments) {
            current.setBackground(current == instrument ? focusBackground : unfocusBackground);
        }
    }
}