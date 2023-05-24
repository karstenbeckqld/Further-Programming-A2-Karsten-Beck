package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
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

    HashMap<String,String> patientData;

    private String patientId;


    public void initialize(){
        patientData = new HashMap<>();


    }

    public void cancelPasswordReset(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent,FXMLUtility.loginFXML);
    }

    public void changePassword(ActionEvent event) throws IOException {

        ObservableList<Patient> patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");



        int numErrors = 0;

        if (this.userName.getText().isEmpty()) {
            this.errorMessage.setText("Username cannot be empty");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
        }

        if (this.password.getText().isEmpty()) {
            this.errorMessage.setText("Password cannot be empty");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.password.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.password.getStyleClass().remove("textFieldError");
        }

        if (!this.confirmPassword.getText().equals(this.password.getText())) {
            this.errorMessage.setText("Password does not match");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.confirmPassword.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.confirmPassword.getStyleClass().remove("textFieldError");
        }

        if (patient==null){
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
           and by using the DatabaseConnection class' static savePatientData() method, saved to the database.  */
        if (numErrors == 0) {
            patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");
            this.patientId = patient.get(0).getPatientId();
            this.patientData.put("password", this.password.getText());


            /* We use a method from the DatabaseConnection class instead of the Record's own save method because of the
               image that gets saved along with the data. While saving patients records works fine with this method, for
               patients themselves we need a more specialised method.  */
            boolean result = DatabaseConnection.updatePassword(this.patientData,this.patientId);

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

                creationConfirmation.setContentText("Password has been changed.");
                creationConfirmation.setHeaderText("Password Change Confirmation");
                Optional<ButtonType> alertResult = creationConfirmation.showAndWait();
                if (alertResult.isPresent()) {
                    if (alertResult.get() == ButtonType.OK) {
                        stageForward(event, FXMLUtility.loginFXML);
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
