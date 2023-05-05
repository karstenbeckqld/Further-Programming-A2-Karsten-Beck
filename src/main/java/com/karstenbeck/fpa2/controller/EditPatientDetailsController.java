package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class EditPatientDetailsController {

    @FXML
    private Button cancel;

    @FXML
    private Button confirm;

    @FXML
    private Button editImage;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField password;

    @FXML
    private ImageView profileImage;

    @FXML
    private TextField userName;

    private String patientId;

    public void initialize() {
        String patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getId();
        ObservableList<Patient> patientDetails = new RecordFinder().where("patientId", patientId).getData("patients");
        this.userName.setText(patientDetails.get(0).getUserName());
        this.firstName.setText(patientDetails.get(0).getFirstName());
        this.lastName.setText(patientDetails.get(0).getLastName());
        this.email.setText(patientDetails.get(0).getEmail());
        this.password.setText(patientDetails.get(0).getPassword());
        /* byte[] imageBlob = patientDetails.get(0).getImage().getBytes();
        System.out.println(Arrays.toString(imageBlob)); */
    }
}