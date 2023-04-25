//**** PACKAGE ****\\
package com.karstenbeck.fpa2.services;

//**** PACKAGE IMPORTS \\

import com.karstenbeck.fpa2.core.MyHealth;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

//**** START FXML UTILITY CLASS ****\\
//This class has the URL links to all our FXML files and provides an easy
//way for other classes to access them by having them as static URLs
public class FXMLUtility {

    //**** FXML URL LINKS ****\\
    public static URL loginFXML = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/login.fxml");

    public static URL registrationFXML = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/register.fxml");

    public static URL patOverview = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/tableView.fxml");


    //**** LOAD SCENE METHOD ****\\
    //This utility helper method accepts a scene url, stage and css file and will combine the three
    //before returning the loaded scene, this streamlines the loading of scene later in the program.
    public static Scene loadScene(URL sceneURL, Stage stage) throws IOException {

        //Create our FXML loader and pass it the scene URL its in constructor
        FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);

        //load the scene into a new scene
        Scene scene =  new Scene(fxmlLoader.load());

        //set the fill color to transparent
        //scene.setFill(Color.TRANSPARENT);

        //add the CSS
        //scene.getStylesheets().add(css);

        //Add the mouse press events to drag the stage around the monitor.
        scene.setOnMousePressed(pressEvent ->{
            scene.setOnMouseDragged(dragEvent ->{
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        //lastly we return the scene
        return scene;
    }

}
