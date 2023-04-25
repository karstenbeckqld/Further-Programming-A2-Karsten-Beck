package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.EmptyInputFieldException;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;

/**
 * The RegistrationController class forms the interface between the register.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class RegistrationController extends Controller {

    /**
     * The userName TextField provides the application with the value for a username the user entered.
     */
    @FXML
    private TextField userName;

    /**
     * The firstName TextField provides the application with the value for a first name the user entered.
     */
    @FXML
    private TextField firstName;

    /**
     * The lastName TextField provides the application with the value for a last name the user entered.
     */
    @FXML
    private TextField lastName;

    /**
     * The password TextField provides the application with the value for a password the user entered.
     */
    @FXML
    private TextField password;

    /**
     * The createUser() method saves a new user to the database when they input the correct fields.
     *
     * @throws EmptyInputFieldException Because not all fields must be blank, an Exception gets thrown if that's the
     *                                  case.
     */
    public void createUser() throws EmptyInputFieldException {

        if (firstName.getText() != null || lastName.getText() != null ||
                userName.getText() != null || password.getText() != null) {
            HashMap<String, String> patData = new HashMap<>();
            patData.put("patientId", "3");
            patData.put("firstName", this.firstName.getText());
            patData.put("lastName", this.lastName.getText());
            patData.put("userName", this.userName.getText());
            patData.put("password", this.password.getText());


            Patient patient = new Patient(patData);
            patient.saveRecord();
            System.out.println(patient);
        } else {
            throw new EmptyInputFieldException("Please enter at least one value!");
        }

    }

    /**
     * The cancelUserCreation() method loads the login screen again.
     *
     * @throws IOException Because it uses the load() method in the stageForward() method, it can throw an IOException
     */
    public void cancelUserCreation(ActionEvent event) throws IOException {
        stageForward(event, FXMLUtility.loginFXML);
    }
}
