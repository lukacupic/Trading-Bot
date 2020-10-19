package com.dormire.trading.gui.instruments;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class InstrumentManager {

    private static final Color focusColor = Color.rgb(31, 31, 31);
    private static final Color unfocusColor = Color.rgb(45, 44, 45);

    private List<Instrument> instruments;
    private VBox instrumentSidebar;

    private Background focusBackground = new Background(new BackgroundFill(focusColor, CornerRadii.EMPTY, Insets.EMPTY));
    private Background unfocusBackground = new Background(new BackgroundFill(unfocusColor, CornerRadii.EMPTY, Insets.EMPTY));

    public InstrumentManager(VBox instrumentSidebar) {
        this.instruments = new LinkedList<>();
        this.instrumentSidebar = instrumentSidebar;
    }

    public void addInstrument(Instrument instrument) {
        instruments.add(instrument);
        instrumentSidebar.getChildren().add(instrument.getBox());
    }

    public void removeInstrument(Instrument instrument) {
        instruments.remove(instrument);
        instrumentSidebar.getChildren().remove(instrument.getBox());
    }

    public boolean contains(String ticker) {
        for (Instrument current : instruments) {
            String currentTicker = current.getTicker();
            if (currentTicker != null && currentTicker.equals(ticker)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void setActive(Instrument instrument) {
        for (Instrument current : instruments) {
            current.getBox().setBackground(current == instrument ? focusBackground : unfocusBackground);
        }
    }
}