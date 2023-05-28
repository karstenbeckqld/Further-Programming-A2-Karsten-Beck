package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.utilities.FXMLUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * The LoginController class forms the interface between the login.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class LoginController extends Controller {

    /**
     * The reference to the buttons used in the LoginController class.
     */
    @FXML
    public Button login, register, forgotPwdButton, exitBtn;

    /**
     * The loginMessage describes a label where we can display text to the user.
     */
    @FXML
    private Label loginMessage;

    /**
     * The password TextField provides the application with the value for a password the user entered.
     */
    @FXML
    private PasswordField password;

    /**
     * The userName TextField provides the application with the value for a username the user entered.
     */
    @FXML
    private TextField userName;

    /**
     * The initialize() method gets executed automatically upon loading the LoginController. It sets up appropriate
     * EventHandlers for the input fields and buttons, so that users can login by pressing the enter key or clicking the
     * mouse.
     */
    public void initialize() {

        /* Setting up he buttons so that the user can use either the mouse or the keyboard to hit enter.  */
        EventHandler<MouseEvent> mouseClickHandler = mouseEvent -> {
            if (MouseButton.PRIMARY.equals(mouseEvent.getButton())) {
                try {
                    loginButtonClick(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        EventHandler<KeyEvent> pressEnterKeyHandler = keyEvent -> {
            if (keyEvent.getCode() == ENTER) {
                try {
                    loginButtonClick(keyEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        userName.setOnKeyPressed(pressEnterKeyHandler);
        login.setOnMousePressed(mouseClickHandler);
        password.setOnKeyPressed(pressEnterKeyHandler);

        this.forgotPwdButton.setOnAction(event -> {
            try {
                stageForward(event, FXMLUtility.resetPassword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.exitBtn.setOnAction(event->{
            /* We first define the stage as the window of the underlying AnchorPane to have a reference for the alert. */
            Stage stage = (Stage) this.exitBtn.getScene().getWindow();

            /* Now we initialise a new alert of type confirmation and define the modality and owner. */
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(stage);

            /* We now set the display text, which will be the selected record's data for confirmation. */
            alert.getDialogPane().setContentText("Are you sure you want to quit MyHealth?");
            alert.setHeaderText("Quit Application");

            /* Now we create the event handler and wait for user input. */
            Optional<ButtonType> buttonResult = alert.showAndWait();
            if (buttonResult.isPresent()) {

            /* If the user clicks the OK button, we quit the application, if not, we stay in the login screen.  */
                if (buttonResult.get() == ButtonType.OK) {
                    Platform.exit();
                    /* If cancel is pressed, we close the alert and do nothing.  */
                } else if (buttonResult.get() == ButtonType.CANCEL) {
                    System.out.println("CANCEL pressed");
                }
            }

        });
    }

    /**
     * The loginButtonClick() method is bound to the login button in login.fxml and performs a basic login check.
     *
     * @param event The event created by the MouseEvent or KeyEvent.
     */
    public void loginButtonClick(Event event) throws IOException {

        //this.loginMessage.setText("");

        int numError = 0;

        if (this.userName.getText().isBlank()) {
            this.loginMessage.setText("Username cannot be empty");
            this.loginMessage.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numError++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
            System.out.println("no empty username");
        }

        if (this.password.getText().isBlank()) {
            this.loginMessage.setText("Password cannot be empty");
            this.loginMessage.getStyleClass().remove("errorHidden");
            this.password.getStyleClass().add("textFieldError");
            numError++;
        } else {
            this.password.getStyleClass().remove("textFieldError");
            System.out.println("no empty password");
        }


        if (numError == 0) {

            /* Retrieve credentials from database table 'patients' by using the 'where' method of the RecordFinder class */
            ObservableList<Patient> patients = new RecordFinder().where("userName", this.userName.getText()).getData("patients");

            /* If we received more than zero records from the database, the username must be valid. */
            if (patients.size() > 0) {

                /* If the entered password and the stored password from the database match, we can proceed and log the
                 * patient in.
                 */
                if (patients.get(0).getPassword().equals(DatabaseConnection.hashPassword(this.password.getText()))) {

                    /*  If the password and username from the entry and in the database match, we set the global active patient to the patient that logged in */
                    MyHealth.getMyHealthInstance().setCurrentPatient(Patient.setPatientByUserName(this.userName.getText()));

                    /* Now we can forward the user to the next scene where they'll get their records displayed. */
                    stageForward(event, FXMLUtility.dashboard);
                } else {

                    /* If not, we display an error message. */
                    this.loginMessage.getStyleClass().remove("errorHidden");
                    this.userName.getStyleClass().add("textFieldError");
                    this.loginMessage.setText("Wrong username or password.");
                }

            } else {
                this.loginMessage.getStyleClass().remove("errorHidden");
                this.userName.getStyleClass().add("textFieldError");
                this.loginMessage.setText("This patient is not registered.");
            }
        } else {
            if (numError > 1) {
                this.loginMessage.setText("Please provide your login details.");
            }
        }
        System.out.println("end of loginButtonClick");
    }


    /**
     * The registerNewPatient() method is bound to the register button in login.fxml and changes the scene to the
     * registration view.
     *
     * @param event The ActionEvent received from the 'Register' button.
     * @throws IOException The method can throw an IOException due to the Event class
     */
    public void registerButtonClick(ActionEvent event) throws IOException {
        stageForward(event, FXMLUtility.register);
    }
}