package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.utilities.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;

/**
 * The MenuController manages a sidebar menu in the Dashboard.
 *
 * @author Karsten Beck
 * @version 1.0 (19/05/2023)
 */
public class MenuController extends Controller {


    @FXML
    private ImageView profileImage;

    @FXML
    private Button editProfile, displayData, addRecord, selectRecords, exit, displayAverages;

    @FXML
    private Label displayName;

    private ObservableList<Patient> patientDetails;

    private static MenuController menuController;

    /**
     * The initialize() method loads relevant data from the database and assigns action events to the buttons.
     */
    public void initialize() {

        /* To be able to access this instance f the MenuController, we assign it to the menuController field. */
        MenuController.menuController = this;

        /* Here we get the patient ID of the currently logged in patient. */
        String patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* Load the patient details for use throughout the class. */
        this.patientDetails = new RecordFinder().where("patientId", patientId).getData("patients");

        /* Here we set the name displayed at the top of the window to the currently logged in patient. */
        this.displayName.setText(patientDetails.get(0).getLastName() + ", " + patientDetails.get(0).getFirstName());

        /* To bring img in scope, we declare ist here. */
        Image img = null;

        /* We load the profile image from the database by using the DatabaseConnection class' static methods. */
        try {
            img = DatabaseConnection.getProfileImage(patientId);
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        /* Now we assign the image to the image view. */
        if (img != null) {
            this.profileImage.setImage(img);
            centerImage();
        } else {
            System.out.println("No profile image available.");
        }

        /* Here we define the action for pressing the 'Display Averages' button. */
        this.displayAverages.setOnAction(event -> {
            try {
                openAverageView(this.displayAverages);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Here we define the action for pressing the 'Load Data' button. */
        this.displayData.setOnAction(event->{
            try {
                loadData(this.displayData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Here we define the action for pressing the 'Edit Profile' button. */
        this.editProfile.setOnAction(event->{
            try {
                editPatientProfile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Here we define the action for pressing the 'Add Record' button. */
        this.addRecord.setOnAction(event->{
            try {
                addPatientRecord();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Here we define the action for pressing the 'Select Records' button. */
        this.selectRecords.setOnAction(event->{
            try {
                selectRecords();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * The addPatientRecord() method allows a user to add a patient record to the database. It calls the addRecord FXML
     * file and controller in a new window.
     *
     * @throws IOException Because we're using the FXMLLoader class, we can get an IOException.
     */
    @FXML
    private void addPatientRecord() throws IOException {

        /* The addPatientRecord() method lets a window pop up to enter values. Therefore, we first define a new stage. */
        Stage addView = new Stage();

        /* Now we load the scene. */
        FXMLLoader loader = new FXMLLoader(FXMLUtility.addRecord);
        Scene scene = new Scene(loader.load());

        /* Here we add the scene to the stage defined earlier. */
        addView.setScene(scene);

        /* Now we load the corresponding controller. */
        AddRecordController addRecordController = loader.getController();

        /* The setStage() method helps us to pass on the stage to the controller. */
        addRecordController.setStage(addView);

        /* Now we show the new stage. */
        addView.show();
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

        /* Now we load the corresponding controller. */
        EditPatientDetailsController editPatientDetailsController = loader.getController();

        /* Finally, show the stage. */
        editPatientProfile.show();
    }

    /**
     * The selectRecords() method opens the recordSelection FXML view and the respective controller. It lets the user
     * select records for export.
     *
     * @throws IOException We use the FXMLLoader class which can throw an IOException.
     */
    public void selectRecords() throws IOException {

        /* The selectRecords() method lets a window pop up to enter values. Therefore, we first define a new stage. */
        Stage selectView = new Stage();

        /* Now we load the scene. */
        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordSelection);
        Scene scene = new Scene(loader.load());

        /* Now we set the scene for the new stage. */
        selectView.setScene(scene);

        /* Now we load the corresponding controller. */
        RecordSelectorController recordSelectorController = loader.getController();

        /* The setStage() method helps us to pass on the stage to the controller. */
        recordSelectorController.setStage(selectView);

        /* Now show the scene. */
        selectView.show();
    }

    /**
     * The openAverageView() method loads the FXML file to display a line chart into the main window of the dashboard.
     *
     * @throws IOException We use the FXMLLoader why we need to throw a possible IOException.
     */
    private void openAverageView(Button button) throws IOException {

        /*Contrary to the other menu items, this one loads the new scene into the main window. Therefore, we first need
         * to clear the old scene by using the resetWindow() method.
         */
        AnchorPane mainWindow = resetWindow(button);

        /* We now load the new view and set it to the same root node we've cleared before. */
        FXMLLoader averageLoader = new FXMLLoader(FXMLUtility.averages);
        AnchorPane averageView = averageLoader.load();
        AnchorPane main = (AnchorPane) mainWindow.getChildren().get(0);

        /* Here we add the new view to the main AnchorPane and load the corresponding controller. */
        main.getChildren().addAll(averageView.getChildren());
        AverageViewController averageViewController = averageLoader.getController();
        main.getStylesheets().add("/com/karstenbeck/fpa2/css/lineChart.css");
    }

    /**
     * The loadData() method removes the current content in the main window and loads the table view.
     *
     * @param button The button instance that calls this method.
     * @throws IOException As we use FXMLLoader, we can encounter an IOException.
     */
    private void loadData(Button button) throws IOException {

        /*As the openAverageView() method, this method loads the new scene into the main window. Therefore, we first need
         * to clear the old scene by using the resetWindow() method.
         */
        AnchorPane mainWindow = resetWindow(button);

        /* We now load the new view and set it to the same root node we've cleared before. */
        FXMLLoader tableViewLoader = new FXMLLoader(FXMLUtility.recordListing);
        AnchorPane averageView = tableViewLoader.load();
        AnchorPane main = (AnchorPane) mainWindow.getChildren().get(0);

        /* Here we add the new view to the main AnchorPane and load the corresponding controller. */
        main.getChildren().addAll(averageView.getChildren());
        TableViewController tableViewController = tableViewLoader.getController();
    }

    /**
     * The resetWindow() method deletes the content of the parent window to avoid overlapping content.
     *
     * @param button The button instance that is calling this method.
     * @return An AnchorPane that can get used to add new content.
     */
    private AnchorPane resetWindow(Button button){
        /* Get the root node of the scene the averages button is contained in */
        AnchorPane mainWindow = (AnchorPane) button.getScene().getRoot();

        /* Now we define the contentContainer to be at root position zero and clear the content */
        AnchorPane contentContainer = (AnchorPane) mainWindow.getChildren().get(0);
        contentContainer.getChildren().clear();

        return mainWindow;
    }

    /**
     * The getMenuControllerInstance() method returns an instance of this MenuController.
     *
     * @return MenuController instance.
     */
    public static MenuController getMenuControllerInstance() {
        return MenuController.menuController;
    }

    /**
     * The signOut() method closes the dashboard and returns the user back to the login screen.
     *
     * @param actionEvent The action event fired by the Sign-Out button.
     * @throws IOException We use the FXMLLoader class which can throw an IOException.
     */
    public void signOut(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent, FXMLUtility.login);
    }

    /**
     * The centerImage() method serves as a helper to center the profile image within the ImageView display. It has been
     * adapted from:
     * <a href="https://stackoverflow.com/questions/32781362/centering-an-image-in-an-imageview">...</a>
     */
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

    /**
     * The reloadMenuController() reloads the MenuController class to update the profile image after changes had been made.
     */
    public void reloadMenuController() {
        initialize();
    }
}
