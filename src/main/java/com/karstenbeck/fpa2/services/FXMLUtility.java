
package com.karstenbeck.fpa2.services;



import com.karstenbeck.fpa2.core.MyHealth;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The FXMLUtility class provides a central point of URL to the required fxml files for various scenes. The URLs are
 * declared static, so that they can get accessed without instantiating the FXMLUtility class itself.
 */
public class FXMLUtility {

    /* URLs to the respective fxml files as public static variables. */
    public static URL loginFXML = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/login.fxml");

    public static URL registrationFXML = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/register.fxml");

    public static URL patOverview = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/tableView.fxml");

    public static URL editView = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/editRecord.fxml");

    public static URL editPatientDetails = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/editPatientDetails.fxml");

    public static URL addRecord = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/addRecord.fxml");

    public static URL recordSelection = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/recordSelector.fxml");

    public static URL saveFileDialogue = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/saveFileDialogue.fxml");


    /* The loadScene() method is a helper method that takes a URL and a Stage and adds the corresponding CSS files to
       provide a single point to add styling to a scene.

       Because only the login screen and the registration scene use styling so far, usage currently is limited and
       practicability is still under investigation. */
    public static Scene loadScene(URL sceneURL, Stage stage) throws IOException {

        //Create our FXML loader and pass it the scene URL its in constructor
        FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);

        //load the scene into a new scene
        Scene scene =  new Scene(fxmlLoader.load());

        //set the fill color to transparent
        scene.setFill(Color.TRANSPARENT);

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
