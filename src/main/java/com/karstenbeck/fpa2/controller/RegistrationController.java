package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.EmptyInputFieldException;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

/**
 * The RegistrationController class forms the interface between the register.fxml file and MyHealth.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class RegistrationController extends Controller {

    public Button cancel;
    private HashMap<String, String> patData;


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

    @FXML
    private TextField email;

    @FXML
    public ImageView image;

    @FXML
    public Button loadImageButton;

    private Stage stage;

    public void initialize() {
        this.patData = new HashMap<>();
    }

    /**
     * The createUser() method saves a new user to the database when they input the correct fields.
     *
     * @throws EmptyInputFieldException Because not all fields must be blank, an Exception gets thrown if that's the
     *                                  case.
     */
    public void createUser() throws EmptyInputFieldException, IOException {
        /* Patient Table:
         * patientId: auto-increment
         * firstName
         * lastName
         * userName
         * password
         * photo
         * email */


        if (this.firstName.getText() != null ||
                this.lastName.getText() != null ||
                this.userName.getText() != null ||
                this.password.getText() != null ||
                this.email != null ||
                this.image != null) {


            patData.put("firstName", this.firstName.getText());
            patData.put("lastName", this.lastName.getText());
            patData.put("userName", this.userName.getText());
            patData.put("password", this.password.getText());
            patData.put("email", this.email.getText());

            DatabaseConnection.preparedStatementQuery(patData);

            /* Patient patient = new Patient(patData);
            patient.saveRecord(); */
            this.firstName.setText("");
            this.lastName.setText("");
            this.userName.setText("");
            this.password.setText("");
            this.email.setText("");
            Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
            creationConfirmation.setContentText("New user created!");
            creationConfirmation.show();
            /* System.out.println(patient); */
        } else {
            throw new EmptyInputFieldException("Please enter at least one value!");
        }

    }

    public void insertImageButtonPress() {

        this.stage = MyHealth.getMyHealthInstance().getStage();

        /* List<Window> windows = Window.getWindows().filtered(Window::isShowing);
        this.stage = (Stage) windows.get(0); */

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a profile picture");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);

        InputStream is;
        try {
            is = new FileInputStream(selectedFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.patData.put("img", selectedFile.getAbsolutePath());

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
