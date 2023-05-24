package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.EmptyInputFieldException;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

/**
 * The RegistrationController class forms the interface between the register.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 2.0 (12/05/2023)
 */
public class RegistrationController extends Controller {

    @FXML
    public Button cancel;

    @FXML
    public ImageView backImg;

    @FXML
    public PasswordField confirmPassword;

    @FXML
    public Label registrationError;

    @FXML
    public Button createUser;

    @FXML
    public AnchorPane anchorPane;

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
    private PasswordField password;

    /**
     * The email TextField provides the application with the value for the email the user entered.
     */
    @FXML
    private TextField email;

    /**
     * The image ImageView displays the user's profile image. It gets set to a default image by the application and
     * updated when the user chooses an image via the loadImage button.
     */
    @FXML
    public ImageView image;

    @FXML
    public Button loadImageButton;

    @FXML
    public Button backBtn;

    private HashMap<String, String> patData;

    /**
     * The initialize() method gets called automatically on class creation and initializes a new global HashMap for patient
     * data. This creation is required because we need the HashMap available across multiple methods. That is also the
     * reason why the patData had been created as instance variable.
     */
    public void initialize() throws URISyntaxException {

        /* First, we initialise the patData field as a new HashMap. */
        this.patData = new HashMap<>();

        /* Because we set a default patient image for every user that registers, we define the file here.  */
        File selectedFile = new File("src/main/resources/com/karstenbeck/fpa2/images/default.png");

        /* Now we're filling the ImageView with this file. */

        try {
            chooseImage(selectedFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        /* If the user doesn't change the photo to their own, we have to keep the path to the current image. Therefore,
           we set the "photo" and "imageFilePath" key's values to the path of the default photo.  */
        this.patData.put("photo", selectedFile.getAbsolutePath());
        this.patData.put("imageFilePath", selectedFile.getAbsolutePath());

        /* If the user decides not to register, they'll be offered a back button here.  */
        this.backBtn.setOnAction(actionEvent -> {
            try {
                stageForward(actionEvent, FXMLUtility.loginFXML);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * The createUser() method saves a new user to the database when they input the correct fields.
     *
     * @throws EmptyInputFieldException Because not all fields must be blank, an Exception gets thrown if that's the
     *                                  case.
     */
    public void createUser(ActionEvent event) throws EmptyInputFieldException, IOException {

        /* We need to check for uniqueness of the username. Hence, we load all database instances of the username
           entered. If this value is greater than 0, we have a username duplication and need to inform the registering
           user.  */
        ObservableList<Patient> patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");

        /* The following code block performs checks on empty input fields and renders the text fields, as well as the
           error message, accordingly. Only of there are no errors, the user can get created.  */

        int numErrors = 0;

        if (this.firstName.getText().isEmpty()) {
            this.registrationError.setText("First name cannot be empty");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.firstName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.firstName.getStyleClass().remove("textFieldError");
        }

        if (this.lastName.getText().isEmpty()) {
            this.registrationError.setText("Last name cannot be empty");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.lastName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.lastName.getStyleClass().remove("textFieldError");
        }

        if (this.userName.getText().isEmpty()) {
            this.registrationError.setText("Username cannot be empty");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
        }

        if (this.password.getText().isEmpty()) {
            this.registrationError.setText("Password cannot be empty");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.password.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.password.getStyleClass().remove("textFieldError");
        }

        if (!this.confirmPassword.getText().equals(this.password.getText())) {
            this.registrationError.setText("Password does not match");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.confirmPassword.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.confirmPassword.getStyleClass().remove("textFieldError");
        }

        if (patient.size() != 0) {
            this.registrationError.setText("Username already taken");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.userName.getStyleClass().remove("textFieldError");
        }

        /* When there are no errors, the values of the entered test fields are added to the HashMap initialised earlier
           and by using the DatabaseConnection class' static savePatientData() method, saved to the database.  */
        if (numErrors == 0) {
            patData.put("firstName", this.firstName.getText());
            patData.put("lastName", this.lastName.getText());
            patData.put("userName", this.userName.getText());
            patData.put("password", this.password.getText());
            patData.put("email", this.email.getText());

            /* We use a method from the DatabaseConnection class instead of the Record's own save method because of the
               image that gets saved along with the data. While saving patients records works fine with this method, for
               patients themselves we need a more specialised method.  */
            boolean result = DatabaseConnection.savePatientData(this.patData);

            /* Now we can clear all fields for new entries. */
            this.firstName.clear();
            this.lastName.clear();
            this.userName.clear();
            this.password.clear();
            this.confirmPassword.clear();
            this.email.clear();

            /* If the patient got saved successfully, we display an alert to confirm the addition to the database. */
            if (result) {
                Stage stage = (Stage) this.anchorPane.getScene().getWindow();

                Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
                creationConfirmation.initModality(Modality.APPLICATION_MODAL);
                creationConfirmation.initOwner(stage);

                creationConfirmation.setContentText("New Patient Created.");
                creationConfirmation.setHeaderText("Creation Confirmation");
                Optional<ButtonType> alertResult = creationConfirmation.showAndWait();
                if (alertResult.isPresent()) {
                    if (alertResult.get() == ButtonType.OK) {
                        stageForward(event, FXMLUtility.loginFXML);
                    }
                }
            } else {
                System.out.println("Patient not created.");
            }
        } else {
            if (numErrors > 1) {
                this.registrationError.setText("There are still errors in your registration.");
            }
        }
    }

    /**
     * The insertImageButtonPress() method lets the user choose an image from a file within the file system of the
     * computer.
     */
    public void insertImageButtonPress() {

        /* Firstly we obtain the reference to the current stage  */
        Stage stage = MyHealth.getMyHealthInstance().getStage();

        /* We display a FileChooser so that the user can pick a file for an image. We allow for the common image file
           extensions. */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(this.loadImageButton.getScene().getWindow());

        if (selectedFile != null) {
            /* Once a file got picked, we display it in the ImageView field as a confirmation for the user. */
            try {
                chooseImage(selectedFile);
            } catch (FileNotFoundException e) {
                // throw new RuntimeException(e);
                System.out.println("No file selected.");
            }


        /* We now add the file path of the chosen image to the "photo" and "imageFilePath" keys of the HamsMap. The
           DatabaseConnection class will deal with converting them into a BLOB. */
            this.patData.put("photo", selectedFile.getAbsolutePath());
            this.patData.put("imageFilePath", selectedFile.getAbsolutePath());
        }
    }

    private void chooseImage(File selectedFile) throws FileNotFoundException {
        InputStream is = new FileInputStream(selectedFile);
        Image image = new Image(is);
        this.image.setFitWidth(150);
        this.image.setPreserveRatio(true);
        this.image.setSmooth(true);
        this.image.setCache(true);
        this.image.setImage(image);
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
