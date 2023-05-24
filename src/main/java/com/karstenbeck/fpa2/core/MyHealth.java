package com.karstenbeck.fpa2.core;

import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Objects;

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

    private Stage primaryStage;

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
            this.primaryStage = primaryStage;
            this.primaryStage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML,primaryStage));

            this.primaryStage.setResizable(false);


            /* Set a title for the stage */
            this.primaryStage.setTitle("Welcome to the MyHealth App!");

            /* Display the stage to the screen. */
            this.primaryStage.show();
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
     * The getStage() method returns the current instance of the primaryStage variable.
     *
     * @return  The current primaryStage
     */
    public Stage getStage() {
        return this.primaryStage;
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