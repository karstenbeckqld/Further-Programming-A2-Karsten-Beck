package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

/**
 * The EditPatientDetailsController class handles all activities related to editing a patient's details.
 *
 * @author Karsten Beck
 * @version 1.0 (12/05/2023)
 */
public class EditPatientDetailsController extends Controller {

    /* We set a reference to the current pane to be able to address the alert window in the confirmButtonPress() method */
    @FXML
    public SplitPane splitPane;

    /* References to the FXML TextFields. */
    @FXML
    private TextField firstName, lastName, userName, password, email;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button editImage;

    @FXML
    private Label errorMessage;

    /* We define a global variable to hold the patient's ID. */
    private String patientId;

    /* The patDataWrite HashMap holds all values to be written to the database. Because it gets used in two different
       methods, it is a global variable. */
    private HashMap<String, String> patDataWrite;

    /* To be able to display a patient's current record, we define a HashMap to store this data retrieved from the database. */
    HashMap<String, Object> patientDataRead;

    /**
     * The initialize() method loads the patient details from the database and fills the fields in the fxml file with this
     * data.
     */
    public void initialize() {
        // this.tableDisplayController = TableDisplayController.getTableDisplayControllerInstance();

        /* To write the edited fields to the database, a HashMap gets instantiated. Because we use different methods for
           the image and for the data (as they're two different processes), we can use a HashMap<String, String> here. */
        this.patDataWrite = new HashMap<>();

        /* Obtain the current patientId from the MyHealth instance. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* To be able to display image and data, we collect the database record in a HashMap<String, Object> */
        this.patientDataRead = DatabaseConnection.getPatientData(this.patientId);

        /* If the patient doesn't change their photo, we store the image file path in the field patDataWrite to avoid
           NullPointerExceptions. The DataBaseConnection class handles the conversion of file path to BLOB.  */
        this.patDataWrite.put("photo", (String) patientDataRead.get("imageFilePath"));

        /* To prefill the fields, we assign the database fields to the textfields. */
        this.userName.setText((String) patientDataRead.get("userName"));

        /* The userName cannot get changed, hence we set the editable property to false. We don't display the password
           as it's stored as a Hash. Therefore, the user must re-enter their password or enter e new one.  */
        this.userName.setEditable(false);
        this.firstName.setText((String) patientDataRead.get("firstName"));
        this.lastName.setText((String) patientDataRead.get("lastName"));
        this.email.setText((String) patientDataRead.get("email"));

        /* Now we load the profile image in a separate database call. */
        Image img = DatabaseConnection.getProfileImage(patientId);
        this.profileImage.setImage(img);
    }

    /**
     * The cancelButtonPress() method closes the current stage and reloads the TableDisplayController to refresh the data
     * displayed. It is assigned in the corresponding FXML file.
     *
     * @param actionEvent The ActionEvent from pressing the button.
     * @throws IOException The FXMLLoader class potentially throws an IOException when the respective fxml file cannot get loaded.
     */
    public void cancelButtonPress(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) splitPane.getScene().getWindow();
        stage.close();
        TableDisplayController tDController = TableDisplayController.getTableDisplayControllerInstance();
        tDController.reloadTable();
    }

    /**
     * The confirmButtonPress() method checks for matching password fields and then writes the content of the
     * TextFields to the Database. It is assigned in the corresponding FXML file.
     */
    public void confirmButtonPress() {
        int numErrors = 0;

        /* Here we check for the password fields to match. */
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


        /* If the password fields match, we either write back the field values retrieved from the database, or add the
           newly entered values to the patDataWrite field to be stored in the database. This accounts for the case of
           a user only updating their image or password. */
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

            /* Because the userName is not editable, we simply write the value from initialize() back to the database. */
            this.patDataWrite.put("userName", this.userName.getText());

            /* Here we take the password value and add it to the database. */
            this.patDataWrite.put("password", this.password.getText());

            /* Now we use the updatePatientDetails() method from the DatabaseConnection to update the Patient */
            boolean result = DatabaseConnection.updatePatientDetails(this.patDataWrite, this.patientId);

            /* To give the user feedback, we have an alert popping up to confirm the successful update. If the user
               presses OK, the update screen will close and the TableDisplayController will update. */

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
    }

    /**
     * The editImageButtonPress() method opens a file chooser and lets the user pick an image for their profile image.
     */
    public void editImageButtonPress() {

        /* A FileChooser opens and lets the user pick an image file.  */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(this.editImage.getScene().getWindow());

        /* Now we set the displayed profile image to the newly chosen image file. */
        if (selectedFile != null) {
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

            /* The database stores two fields for an image, the path itself and the image as a BLOB. Because the
            DatabaseConnection handles the image conversion to a BLOB, we provide the confirmButtonPress() method both
            variables as a String. As we use a global field (patDataWrite), we can add these values here, and they'll be
            saved along.*/
            this.patDataWrite.put("photo", selectedFile.getAbsolutePath());
            this.patDataWrite.put("imageFilePath", selectedFile.getAbsolutePath());
        } else {
            System.out.println("Image not changed.");
            this.patDataWrite.put("photo", (String) patientDataRead.get("imageFilePath"));
            this.patDataWrite.put("imageFilePath", (String) patientDataRead.get("imageFilePath"));
        }
    }
}