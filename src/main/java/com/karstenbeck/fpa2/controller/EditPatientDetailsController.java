package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

public class EditPatientDetailsController extends Controller {

    @FXML
    public SplitPane splitPane;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private TextField email;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button cancel;

    @FXML
    private Button confirm;

    @FXML
    private Button editImage;

    @FXML
    private Label errorMessage;


    private String patientId;

    private HashMap<String, String> patDataWrite;

    HashMap<String, Object> patientDataRead;

    private TableDisplayController tableDisplayController;

    public void initialize() {
        this.tableDisplayController = TableDisplayController.getTableDisplayControllerInstance();
        this.patDataWrite = new HashMap<>();
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        this.patientDataRead = DatabaseConnection.getPatientData(this.patientId);

        this.patDataWrite.put("photo", (String) patientDataRead.get("imageFilePath"));

        this.userName.setText((String) patientDataRead.get("userName"));
        this.userName.setEditable(false);
        this.firstName.setText((String) patientDataRead.get("firstName"));
        this.lastName.setText((String) patientDataRead.get("lastName"));
        this.email.setText((String) patientDataRead.get("email"));
        // this.password.setText((String) patientDataRead.get("password"));

        Image img = DatabaseConnection.getProfileImage(patientId);
        this.profileImage.setImage(img);

        System.out.println(patientDataRead);

    }

    public void cancelButtonPress(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent, FXMLUtility.patOverview);
    }

    public void confirmButtonPress(ActionEvent actionEvent) throws IOException {
        int numErrors = 0;


        if (this.password.getText().isEmpty()) {
            this.errorMessage.setText("Password is required.");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.errorMessage.getStyleClass().add("textFieldError");
            this.password.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.password.getStyleClass().remove("textFieldError");
        }

        if (!this.confirmPassword.getText().equals(this.password.getText())) {
            this.errorMessage.setText("Password does not match.");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.errorMessage.getStyleClass().add("textFieldError");
            this.confirmPassword.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.confirmPassword.getStyleClass().remove("textFieldError");
        }


        if (numErrors == 0) {

            if (!this.firstName.getText().isEmpty()) {
                this.patDataWrite.put("firstName", this.firstName.getText());
            } else {
                this.patDataWrite.put("firstName", (String) this.patientDataRead.get("firstName"));
            }

            if (!this.lastName.getText().isEmpty()) {
                this.patDataWrite.put("lastName", this.lastName.getText());
            } else {
                this.patDataWrite.put("lastName", (String) this.patientDataRead.get("lastName"));
            }

            if (!this.email.getText().isEmpty()) {
                this.patDataWrite.put("email", this.email.getText());
            } else {
                this.patDataWrite.put("email", (String) this.patientDataRead.get("email"));
            }


            this.patDataWrite.put("userName", this.userName.getText());
            this.patDataWrite.put("password", this.password.getText());


            boolean result = DatabaseConnection.updatePatientDetails(this.patDataWrite, this.patientId);

            if (result) {
                Stage stage = (Stage) splitPane.getScene().getWindow();

                Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
                creationConfirmation.initModality(Modality.APPLICATION_MODAL);
                creationConfirmation.initOwner(stage);

                creationConfirmation.setContentText("Your details have been updated.");
                creationConfirmation.setHeaderText("Update Confirmation");
                Optional<ButtonType> alertResult = creationConfirmation.showAndWait();
                if (alertResult.isPresent()) {
                    if (alertResult.get() == ButtonType.OK) {
                        stage.close();
                        TableDisplayController tDController = TableDisplayController.getTableDisplayControllerInstance();
                        tDController.reloadTable();
                    }
                }

            } else {
                System.out.println("Patient details not updated.");
            }
        } else {
            if (numErrors > 1) {
                this.errorMessage.setText("There are errors in the form.");
            }
        }



       /*  TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
        tDC.reloadTable(); */

    }

    public void editImageButtonPress(ActionEvent actionEvent) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(this.editImage.getScene().getWindow());

        Image image;
        try {
            InputStream is = new FileInputStream(selectedFile);
            image = new Image(is);
            this.profileImage.setFitWidth(150);
            this.profileImage.setPreserveRatio(true);
            this.profileImage.setSmooth(true);
            this.profileImage.setCache(true);
            this.profileImage.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.patDataWrite.put("photo", selectedFile.getAbsolutePath());
        this.patDataWrite.put("imageFilePath", selectedFile.getAbsolutePath());
    }
}