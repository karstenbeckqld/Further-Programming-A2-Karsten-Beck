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
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

/**
 * The RegistrationController class forms the interface between the register.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
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
    private TextField password;

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

    private Stage stage;

    private HashMap<String, String> patData;

    /**
     * The initialize() method gets called automatically on class creation and initializes a new global HashMap for patient
     * data. This creation is required because we need the HashMap available across multiple methods. That is also the
     * reason why the patData had been created as instance variable.
     */
    public void initialize() {

        this.patData = new HashMap<>();
        File selectedFile = new File("images/default.png");

        try {
            InputStream is = new FileInputStream(selectedFile);
            Image image = new Image(is);
            this.image.setFitWidth(150);
            this.image.setPreserveRatio(true);
            this.image.setSmooth(true);
            this.image.setCache(true);
            this.image.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.patData.put("photo", selectedFile.getAbsolutePath());
        this.patData.put("imageFilePath", selectedFile.getAbsolutePath());

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


        ObservableList<Patient> patient = new RecordFinder().where("userName", this.userName.getText()).getData("patients");


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

        if (this.userName.getText().isEmpty() ) {
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

        if (patient.size() !=0 ){
            this.registrationError.setText("Username already taken");
            this.registrationError.getStyleClass().remove("errorHidden");
            this.userName.getStyleClass().add("textFieldError");
            numErrors++;
        } else{
            this.userName.getStyleClass().remove("textFieldError");
        }


        if (numErrors == 0) {
            patData.put("firstName", this.firstName.getText());
            patData.put("lastName", this.lastName.getText());
            patData.put("userName", this.userName.getText());
            patData.put("password", this.password.getText());
            patData.put("email", this.email.getText());

            boolean result = DatabaseConnection.savePatientData(this.patData);

            this.firstName.clear();
            this.lastName.clear();
            this.userName.clear();
            this.password.clear();
            this.confirmPassword.clear();
            this.email.clear();

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
                        stageForward(event,FXMLUtility.loginFXML);
                    }
                }


            } else {
                System.out.println("Patient not created.");
            }
        } else {
            if(numErrors>1) {
                this.registrationError.setText("There are still errors in your registration.");
            }
        }
    }

    public void insertImageButtonPress() {

        this.stage = MyHealth.getMyHealthInstance().getStage();


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(this.loadImageButton.getScene().getWindow());

        try {
            InputStream is = new FileInputStream(selectedFile);
            Image image = new Image(is);
            this.image.setFitWidth(150);
            this.image.setPreserveRatio(true);
            this.image.setSmooth(true);
            this.image.setCache(true);
            this.image.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.patData.put("photo", selectedFile.getAbsolutePath());
        this.patData.put("imageFilePath", selectedFile.getAbsolutePath());

        System.out.println(this.patData.get("imageFilePath"));


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
