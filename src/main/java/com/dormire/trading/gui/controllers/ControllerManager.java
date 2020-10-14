package com.dormire.trading.gui.controllers;

public class ControllerManager {

    private static final ControllerManager instance = new ControllerManager();

    private MainController mainController;
    private PromptController promptController;

    private ControllerManager() {
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public PromptController getPromptController() {
        return promptController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public static ControllerManager getInstance() {
        return instance;
    }

    public void setPromptController(PromptController promptController) {
        this.promptController = promptController;
    }
}
