package com.karstenbeck.fpa2.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The Controller class is the parent class to the controllers that interface between the fxml files and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class Controller {

    /**
     * The stageForward() method provides the child classes with a method to create a new stage.
     *
     * @param event      The link to the fxml file for the stage to be crated as String
     * @throws IOException  The method can throw an IOException due to the load() method used
     */
    public void stageForward(Event event, URL forwardTo) throws IOException {

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(forwardTo);
        stage.setScene(new Scene(fxmlLoader.load()));
    }

    public void createPopUp(Event event, URL forwardTo) throws IOException{
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(forwardTo);
    }
}
