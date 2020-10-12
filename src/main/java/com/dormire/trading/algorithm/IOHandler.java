package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.utils.IOUtil;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that provides input-output communication with the user.
 */
public class IOHandler {

    /**
     * The internal Scanner object.
     */
    private Scanner sc;

    public IOHandler() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays a GUI alert with the given message.
     *
     * @param message the message
     */
    public void showAlert(String message) {
        IOUtil.showAlert(message);
    }

    /**
     * Displays a GUI alert with the given message and writes the prompt to the
     * console. The method "prepares" the console for a user's input.
     *
     * @param message the message
     */
    public void showInputAlert(String message) {
        IOUtil.showAlert(message);
        IOUtil.showPrompt();
    }

    /**
     * Plays the default notification sound.
     *
     * @throws IOException if an error occurs while playing audio
     */
    public void playSound() throws IOException {
        IOUtil.playSound();
    }

    /**
     * Prompts the user to provide a string input through the standard input mechanism.
     * Each prompt to the user first displays a message, then displays the prompt symbol
     * in a new line, expecting an input value.
     *
     * @param message the message to display to the user
     * @return the user's string input
     */
    public String getString(String message) {
        showInputAlert(message);
        return sc.nextLine();
    }

    /**
     * Prompts the user to provide a double input through the standard input mechanism.
     * Each prompt to the user first displays a message, then displays the prompt symbol
     * in a new line, expecting an input value.
     *
     * @param message the message to display to the user
     * @return the user's double input
     */
    public double getDouble(String message) {
        showInputAlert(message);
        return Double.parseDouble(sc.nextLine());
    }

    /**
     * Prompts the user to provide an integer input through the standard input mechanism.
     * Each prompt to the user first displays a message, then displays the prompt symbol
     * in a new line, expecting an input value.
     *
     * @param message the message to display to the user
     * @return the user's integer input
     */
    public int getInteger(String message) {
        showInputAlert(message);
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Checks if the user has entered the provided input.
     *
     * @param input the user's input
     * @return true if the user has entered the provided input; false otherwise
     */
    public boolean isInput(String input) {
        return sc.nextLine().equals(input);
    }
}
