package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;

public class MenuController extends Controller {

    @FXML
    private ImageView profileImage;

    @FXML
    private Button editProfile;

    @FXML
    private Button addRecord;

    @FXML
    private Button selectRecords;

    @FXML
    private Button exit;

    @FXML
    private Label displayName;

    private ObservableList<Patient> patientDetails;

    String patientId;

    private static MenuController menuController;

    public void initialize() {

        MenuController.menuController = this;

        /* Here we get the patient ID of the currently logged in patient. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* Load the patient details for use throughout the class. */
        this.patientDetails = new RecordFinder().where("patientId", this.patientId).getData("patients");

        /* We use this patient ID to obtain the patient's details from the database as an observable list. */
        // ObservableList<Patient> patientDetails = new RecordFinder().where("patientId", this.patientId).getData("patients");

        /* Here we set the name displayed at the top of the window to the currently logged in patient. */
        this.displayName.setText(patientDetails.get(0).getLastName() + ", " + patientDetails.get(0).getFirstName());

        /* We now set the image for the ImageView in the sidebar by getting the image data from the database via the
           patient ID. */
        Image img = DatabaseConnection.getProfileImage(this.patientId);
        if (img != null) {
            this.profileImage.setImage(img);
            centerImage();
        } else {
            System.out.println("No profile image available.");
        }
    }

    /**
     * The addPatientRecord() method allows a user to add a patient record to the database. It calls the addRecord FXML
     * file and controller in a new window.
     *
     * @throws IOException Because we're using the FXMLLoader class, we can get an IOException.
     */
    @FXML
    private void addPatientRecord() throws IOException {
        Stage addView = new Stage();
        FXMLLoader loader = new FXMLLoader(FXMLUtility.addRecord);
        Scene scene = new Scene(loader.load());
        addView.setScene(scene);
        AddRecordController addRecordController = loader.getController();
        addRecordController.setStage(addView);
        addView.show();
    }

    /**
     * The closeTable() method closes the table view and returns the user back to the login screen.
     *
     * @param actionEvent The action event fired by the Sign-Out button.
     * @throws IOException We use the FXMLLoader class which can throw an IOException.
     */
    public void signOut(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent, FXMLUtility.loginFXML);
    }

    /**
     * The editPatientProfile() method allows a user to change their profile data. It opens the editPatientDetails FXML
     * file.
     *
     * @throws IOException Because we use the FXMLLoader class, we can encounter an IOException.
     */
    public void editPatientProfile() throws IOException {

        /* To customise the popup window, we use the patient's first and last names to display in the window header. */
        String firstName = this.patientDetails.get(0).getFirstName();
        String lastName = this.patientDetails.get(0).getLastName();

        /* Now we create a new stage to display the edit fields amd load the corresponding controller. */
        Stage editPatientProfile = new Stage();
        editPatientProfile.setTitle("Edit Profile for: " + firstName + " " + lastName);

        FXMLLoader loader = new FXMLLoader(FXMLUtility.editPatientDetails);
        Scene scene = new Scene(loader.load());
        editPatientProfile.setScene(scene);

        EditPatientDetailsController editPatientDetailsController = loader.getController();

        editPatientProfile.show();
    }

    /**
     * The selectRecords() method opens the recordSelection FXML view and the respective controller. It lets the user
     * select records for export.
     *
     * @throws IOException We use the FXMLLoader class which can throw an IOException.
     */
    public void selectRecords() throws IOException {
        Stage selectView = new Stage();
        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordSelection);
        Scene scene = new Scene(loader.load());
        selectView.setScene(scene);
        RecordSelectorController recordSelectorController = loader.getController();
        recordSelectorController.setStage(selectView);
        selectView.show();
    }

    private void centerImage() {
        /* First we get the image from the image view. */
        Image image = this.profileImage.getImage();

        /* If it's not null, we calculate the x and y ratios of the image and define a coefficient that we can multiply
           the image width and height with. Then we calculate the ratio width and height and finally set the X and Y
           positions of the image to the relative values. */
        if (image != null) {
            double width;
            double height;

            double ratioX = this.profileImage.getFitWidth() / image.getWidth();
            double ratioY = this.profileImage.getFitHeight() / image.getHeight();

            double coeff = Math.min(ratioX, ratioY);

            width = image.getWidth() * coeff;
            height = image.getHeight() * coeff;

            this.profileImage.setX((this.profileImage.getFitWidth() - width) / 2);
            this.profileImage.setY((this.profileImage.getFitHeight() - height) / 2);
        }
    }

    public static MenuController getMenuControllerInstance() {
        return MenuController.menuController;
    }

    public void reloadMenuController() throws FileNotFoundException {
        initialize();
    }
}
