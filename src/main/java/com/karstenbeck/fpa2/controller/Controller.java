package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
     * @param resource      The link to the fxml file for the stage to be crated as String
     * @throws IOException  The method can throw an IOException due to the load() method used
     */
    public void stageForward(String resource) throws IOException {
        Stage redirectStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MyHealth.class.getResource(resource));
        redirectStage.setScene(new Scene(fxmlLoader.load()));

        redirectStage.getProperties().put("controller", fxmlLoader.getController());
        redirectStage.show();
    }
}
