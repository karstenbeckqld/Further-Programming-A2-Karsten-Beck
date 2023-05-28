package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.utilities.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * The PasswordResetController class lets a user reset the password to access the app.
 *
 * @author Karsten Beck
 * @version 1.0 (21/05/2023)
 */
public class PasswordResetController extends Controller {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField password;

    @FXML
    private TextField userName;

    @FXML
    private Label errorMessage;

    private HashMap<String, String> patientData;

    private String patientId;


    /**
     * The initialise() method initialises a new HashMap we can use within the class.
     */
    public void initialize() {

        /* We initialise the patientData HashMap. */
        this.patientData = new HashMap<>();
    }

    /**
     * The cancelPasswordReset() method cancels the process of resetting the password.
     *
     * @param actionEvent The ActionEvent triggering the method.
     * @throws IOException As we use the FXMLLoader, we can encounter an IOException.
     */
    public void cancelPasswordReset(ActionEvent actionEvent) throws IOException {

        /* The stageForward() method returns the user to the login screen. */
        stageForward(actionEvent, FXMLUtility.login);
    }

    /**
     * The changePassword() method performs the change of password.
     *
     * @param event The event that triggered this method.
     * @throws IOException The use of the FXMLLoader class can cause an IOException.
     */
    public void changePassword(ActionEvent event) throws IOException {



        /* Now we have to perform a series of checks to make sure the password and username are valid. */
        int numErrors = 0;

        /* First we must check that the username field is not empty as we need it to determine which user wants to reset
         * their password.
         */
        if (this.userName.getText().isEmpty()) {
            this.errorMessage.setText("Username cannot be empty");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
        }

        /* Now we need to check for an empty password field. */
        if (this.password.getText().isEmpty()) {
            this.errorMessage.setText("Password cannot be empty");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.password.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.password.getStyleClass().remove("textFieldError");
        }

        /* We also need to check for a match of the new password and it's confirmation. */
        if (!this.confirmPassword.getText().equals(this.password.getText())) {
            this.errorMessage.setText("Password does not match");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.confirmPassword.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.confirmPassword.getStyleClass().remove("textFieldError");
        }

        /* We now need the patient data of the patient that wants to reset the password. We load this data by using
         * the RecordFinder class and search for the username entered.
         */
        ObservableList<Patient> patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");

        /* If the patient variable comes back null, the username does not exist, the same is true for an empty variable. */
        if (patient == null) {
            this.errorMessage.setText("Username is not registered");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
            if (patient.size() == 0) {
                this.errorMessage.setText("Username does not exist");
                this.errorMessage.getStyleClass().remove("errorHidden");
                this.userName.getStyleClass().add("textFieldError");
                numErrors++;
            } else {
                this.userName.getStyleClass().remove("textFieldError");
            }
        }



        /* When there are no errors, the values of the entered test fields are added to the HashMap initialised earlier
         * and by using the DatabaseConnection class' static savePatientData() method, saved to the database.
         * */
        if (numErrors == 0) {
            patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");
            this.patientId = patient.get(0).getPatientId();
            this.patientData.put("password", this.password.getText());


            /* We use a method from the DatabaseConnection class instead of the Record's own save method because of the
             * image that gets saved along with the data. While saving patients records works fine with this method, for
             * patients themselves we need a more specialised method.
             */
            boolean result = DatabaseConnection.updatePassword(this.patientData, this.patientId);

            /* Now we can clear all fields for new entries. */
            this.userName.clear();
            this.password.clear();
            this.confirmPassword.clear();


            /* If the patient got saved successfully, we display an alert to confirm the addition to the database. */
            if (result) {
                Stage stage = (Stage) this.anchorPane.getScene().getWindow();

                Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
                creationConfirmation.initModality(Modality.APPLICATION_MODAL);
                creationConfirmation.initOwner(stage);

                /* Here we set the title and message of the alert box. */
                creationConfirmation.setContentText("Password has been changed.");
                creationConfirmation.setHeaderText("Password Change Confirmation");

                /* Now we display the alert and wait for the user to make an input. */
                Optional<ButtonType> alertResult = creationConfirmation.showAndWait();

                /* When the user presses OK, the login screen gets displayed. */
                if (alertResult.isPresent()) {
                    if (alertResult.get() == ButtonType.OK) {
                        stageForward(event, FXMLUtility.login);
                    }
                }
            } else {
                System.out.println("Password not reset.");
            }
        } else {
            if (numErrors > 1) {
                this.errorMessage.setText("There are errors in the form.");
            }
        }
    }
}
