package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * The LoginController class forms the interface between the login.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class LoginController extends Controller {

    /**
     * The reference to the login button in the LoginController class.
     */
    @FXML
    public Button login;

    /**
     * The reference to the register button in the LoginController class.
     */
    @FXML
    public Button register;
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
     * The initialize() method gets executed automatically uon loading the LoginController. It sets up appropriate
     * EventHandlers for the input fields and buttons.
     */
    public void initialize() {

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
    }

    /**
     * The loginButtonClick() method is bound to the login button in login.fxml and performs a basic login check.
     */
    public void loginButtonClick(Event event) throws IOException {
        if (this.userName.getText().isBlank()) {
            this.loginMessage.setText("Please enter a Username.");
        } else if (this.password.getText().isBlank()) {
            this.loginMessage.setText("Your Password is required.");
        } else {

            /* Retrieve credentials from database table 'patients' by using the 'where' method of the RecordFinder class */
            ObservableList<Patient> isPatient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");

            if (isPatient.size() > 0) {

                if (this.password.getText().equals(isPatient.get(0).getPassword()) && this.userName.getText().equals(isPatient.get(0).getUserName())) {

                    /* If the password and username from the entry and in the database match, we set the global active patient to the patient that logged in */
                    MyHealth.getMyHealthInstance().setCurrentPatient(Patient.setPatientByUserName(this.userName.getText()));

                    /* Now we can forward the user to the next scene where they'll get their records displayed. */
                    stageForward(event, FXMLUtility.patOverview);
                }

            } else {
                this.loginMessage.setText("Wrong username or password. Please try again.");
            }
        }
    }

    /**
     * The registerNewPatient() method is bound to the register button in login.fxml and changes the scene to the
     * registration view.
     *
     * @param event         The ActionEvent received from the 'Register' button.
     * @throws IOException  The method can throw an IOException due to the Event class
     */
    public void registerButtonClick(ActionEvent event) throws IOException {
        stageForward(event, FXMLUtility.registrationFXML);
    }
}