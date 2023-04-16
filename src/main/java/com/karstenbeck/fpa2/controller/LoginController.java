package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.karstenbeck.fpa2.model.Patient;
import java.io.IOException;

/**
 * The LoginController class forms the interface between the login.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class LoginController extends Controller{

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
     * The loginButtonClick() method is bound to the login button in login.fxml and performs a basic login check.
     */
    public void loginButtonClick() {
        if (this.userName.getText().isBlank()) {
            this.loginMessage.setText("Please enter a Username.");
        } else if (this.password.getText().isBlank()) {
            this.loginMessage.setText("Your Password is required.");
        } else {

            MyHealth.getMyHealthInstance().setCurrentPatient(Patient.setPatientByUserName(this.userName.getText()));
            System.out.println(MyHealth.getMyHealthInstance().getCurrentPatient().toString());

            /* Now open the patient's record overview stage. */
        }
    }

    /**
     * The registerNewPatient() method is bound to the register button in login.fxml and changes the scene to the
     * registration view.
     */
    public void registerButtonClick() throws IOException {
        stageForward("/com/karstenbeck/fpa2/register.fxml");
    }
}