package com.dormire.trading.gui.controller;

public class ControllerMediator {

    private static final ControllerMediator instance = new ControllerMediator();

    private MainController mainController;
    private PromptController promptController;

    private ControllerMediator() {
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

    public static ControllerMediator getInstance() {
        return instance;
    }

    public void setPromptController(PromptController promptController) {
        this.promptController = promptController;
    }
}
