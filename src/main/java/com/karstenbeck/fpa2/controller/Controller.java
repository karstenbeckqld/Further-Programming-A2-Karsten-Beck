package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
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
     * @param event The link to the fxml file for the stage to be crated as String
     * @throws IOException The method can throw an IOException due to the load() method used
     */
    public void stageForward(Event event, URL forwardTo) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(forwardTo);
        stage.setScene(new Scene(fxmlLoader.load()));
        centerStage(stage);
    }

    private void centerStage(Stage stage) {
        Rectangle2D stageBounds = Screen.getPrimary().getVisualBounds();;
        double width = (stageBounds.getWidth() - stage.getWidth()) / 2;
        double height = (stageBounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(width);
        stage.setY(height);
    }
}
