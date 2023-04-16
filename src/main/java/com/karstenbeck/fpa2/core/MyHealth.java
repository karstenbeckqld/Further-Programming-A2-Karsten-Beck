package com.karstenbeck.fpa2.core;

import com.karstenbeck.fpa2.model.Patient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Driver class which is the main point of execution in this application
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class MyHealth extends Application {

    /**
     * The variable myHealthInstance stores the instance of the MyHealth class, so that other classes can access the MyHealth object.
     */
    private static MyHealth myHealthInstance;

    /**
     * The variable patient stores an instance of the Patient class. This allows for other classes to know who is the current patient using the app.
     */
    private Patient patient;

    /**
     * Default constructor to create an object of the MyHealth class.
     */
    public MyHealth() {
        MyHealth.myHealthInstance = this;
    }

    /**
     * The start() method inherited from the abstract JavaFX Application class.
     *
     * @param primaryStage The primary stage as Stage object
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("/com/karstenbeck/fpa2/login.fxml"));

            /* Create the Scene */
            Scene scene = new Scene(root, 300, 300);
            scene.getStylesheets().add(getClass().getResource("/com/karstenbeck/fpa2/application.css").toExternalForm());

            /* Place the scene on the stage */
            primaryStage.setScene(scene);

            /* Set a title for the */
            primaryStage.setTitle("Welcome to MyHealth App!");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The getCurrentPatient() method returns the current patient to the caller.
     *
     * @return The patient logged into the app as Patient
     */
    public Patient getCurrentPatient() {
        return this.patient;
    }

    /**
     * The setCurrentPatient() method sets the patient instance variable to the current patient logged into the app.
     *
     * @param patient The logged in patient as Patient object
     */
    public void setCurrentPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * The getInstance() method returns the current MyHealth instance to the caller
     *
     * @return The myHealthInstance instance variable as MyHealth object
     */
    public static MyHealth getMyHealthInstance() {
        return MyHealth.myHealthInstance;
    }

    /**
     * The method to start the program. Executes the launch() method from the Application class.
     *
     * @param args Array of strings representing command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

}