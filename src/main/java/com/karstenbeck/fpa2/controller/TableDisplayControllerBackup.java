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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * The TableViewController class displays the records stored for a patient, as well as the patient's name and
 * profile image. .
 *
 * @author Karsten Beck
 * @version 1.0 (13/05/2023)
 */
public class TableDisplayControllerBackup extends Controller {

    @FXML
    public javafx.scene.layout.AnchorPane AnchorPane;

    @FXML
    public Label displayName;

    @FXML
    public ImageView profileImage;

    @FXML
    public Button edit;

    @FXML
    public Button exit;

    @FXML
    public Button addRecord;

    @FXML
    private TableView<PatientRecord> tableView;

    @FXML
    private TableColumn<PatientRecord, String> date, time, weight, temp, sysBp, diaBp, comment;

    @FXML
    private TableColumn<PatientRecord, Button> buttons;

   /*  private int recordId; */

    private static TableDisplayControllerBackup tDController;

    private ObservableList<Patient> patientDetails;

    String patientId;

    /**
     * The initialize() method retrieves the patient ID for the currently registered patient from the MyHealth class and
     * sets the display name as well as creates the TableView and the ImageView of the profile image. It also adds a
     * patient's records to the TableView.
     */
    @FXML
    public void initialize() {

        /* We often refer back to the TableViewController from other classes to reload the table data. Therefore, we
           assign the current TableViewController class to a variable to be able to access it.  */
        TableDisplayControllerBackup.tDController = this;

        /* Here we get the patient ID of the currently logged in patient. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* Load the patient details for use throughout the class. */
        this.patientDetails = new RecordFinder().where("patientId", this.patientId).getData("patients");

        /* We use this patient ID to obtain the patient's details from the database as an observable list. */
        ObservableList<Patient> patientDetails = new RecordFinder().where("patientId", this.patientId).getData("patients");

        /* Here we set the name displayed at the top of the window to the currently logged in patient. */
        this.displayName.setText(patientDetails.get(0).getLastName() + ", " + patientDetails.get(0).getFirstName());

        /* Now we load all patient records available in the database. */
        ObservableList<PatientRecord> patientRecords = new RecordFinder().where("patientId", this.patientId).getData("records");

        /* Assign these records to the TableView. */
        this.tableView.setItems(patientRecords);

        /* Setting the CellValueFactories for the table columns. */
        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        /* We now set the image for the ImageView in the sidebar by getting the image data from the database via the
           patient ID. */
        Image img = DatabaseConnection.getProfileImage(this.patientId);
        if (img != null) {
            this.profileImage.setImage(img);
            centerImage();
        } else {
            System.out.println("No profile image available.");
        }


        /* Here we connect the Edit and Delete buttons for each table row to respective ActionEvents. This approach got
           adapted from:
           https://stackoverflow.com/questions/44696775/how-to-add-two-buttons-in-a-tablecolumn-of-tableview-javafx */
        this.buttons.setCellFactory(param -> new TableCell<>() {

            /* First we define which buttons within the TableCells must be addressed. */
            private final Button editButton = new Button("edit");
            private final Button deleteButton = new Button("delete");

            /* Now we override the respective updateItem methods for each button. */
            @Override
            public void updateItem(Button patient, boolean empty) {
                super.updateItem(patient, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {

                    deleteButton.getStyleClass().add("deleteButton");

                    /* When the Delete button gets pressed, we get the corresponding entry index and record. */
                    deleteButton.setOnAction(event -> {
                        PatientRecord patientRecord = getTableView().getItems().get(getIndex());
                        System.out.println(patientRecord.toString());

                        /* The record gets passed on to the deletePatientRecord() method. */
                        deletePatientRecord(patientRecord);
                    });

                    editButton.getStyleClass().add("editButton");

                    /* When the Edit button gets pressed, we get the corresponding entry index and record. */
                    editButton.setOnAction(event -> {
                        PatientRecord patientRecord = getTableView().getItems().get(getIndex());
                        try {

                            /* The record gets passed on to the editPatientRecord() method. The editPatientRecord()
                               method throes an IOException which gets handled in a try-catch block. */
                            editPatientRecord(patientRecord);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    /* We now add the buttons to a Hbox and display them within a table cell. The Hbox serves as
                       container for better styling. */
                    HBox buttonDisplay = new HBox(editButton, deleteButton);
                    buttonDisplay.getStyleClass().add("hbox");
                    setGraphic(buttonDisplay);
                }
            }
        });
    }

    /**
     * The editPatientRecord() method gets called from the Edit button inside a table row and allows for a record to get
     * edited. It opens the Edit view, defined by the editView FXML file and passes the record data on to this class.
     *
     * @param patientRecord The patient record displayed in the table row the button is contained in.
     * @throws IOException  Because we're using the FXMLLoader class, we can encounter an IOException.
     */
    private void editPatientRecord(PatientRecord patientRecord) throws IOException {

        /* Create a new stage for the edit view and pass on the record that has been detected by the button action event. */
        Stage editView = new Stage();
        editView.setMinWidth(300);
        editView.setMinHeight(300);
        editView.setTitle("Edit Record number: " + patientRecord.getRecordId());

        FXMLLoader loader = new FXMLLoader(FXMLUtility.editView);
        Scene scene = new Scene(loader.load());

        editView.setScene(scene);

        EditRecordController editRecordController = loader.getController();
        editRecordController.prefillRecord(patientRecord.getRecordId());
        editRecordController.setStage(editView);

        editView.show();
    }

    /**
     * The addPatientRecord() method allows a user to add a patient record to the database. It calls the addRecord FXML
     * file and controller in a new window.
     *
     * @throws IOException  Because we're using the FXMLLoader class, we can get an IOException.
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
     * The deletePatientRecord() receives a patient record and deletes it from the database. It uses the Record class'
     * deleteRecord() method to perform this action and receives the patient record t delete from the action event of
     * the button pressed in the table row.
     *
     * @param patientRecord The patient record to delete as type PatientRecord.
     */
    private void deletePatientRecord(PatientRecord patientRecord) {

        /* We first define the stage as the window of the underlying AnchorPane to have a reference for the alert. */
        Stage stage = (Stage) AnchorPane.getScene().getWindow();

        /* Now we initialise a new alert of type confirmation and define the modality and owner. */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        /* We now set the display text, which will be the selected record's data for confirmation. */
        alert.getDialogPane().setContentText("Are you sure you want to delete this record:\n"
                + "Date        : " + patientRecord.getDate() + "\n"
                + "Time        : " + patientRecord.getTime() + "\n"
                + "Weight      : " + patientRecord.getWeight() + "\n"
                + "Temperature : " + patientRecord.getTemperature() + "\n"
                + "systolic BP : " + patientRecord.getSysBp() + "\n"
                + "diastolic BP: " + patientRecord.getDiaBp());
        alert.setHeaderText("Deleting Patient Record");

        /* Now we create the event handler and wait for user input. */
        Optional<ButtonType> buttonResult = alert.showAndWait();
        if (buttonResult.isPresent()) {

            /* If the user clicks the OK button, we delete the record from the database using the deleteRecord() method
               from the parent class  */
            if (buttonResult.get() == ButtonType.OK) {
                boolean result = patientRecord.deleteRecord("records", "recordId", patientRecord.getRecordId());
                if (result) {
                    this.reloadTable();
                }

                /* If cancel is pressen, we close the alert and do nothing.  */
            } else if (buttonResult.get() == ButtonType.CANCEL) {
                System.out.println("CANCEL pressed");
            }
        }
    }

    /**
     * The reloadTable() method reloads the table view by calling the initialize() method again.
     * */

    public void reloadTable() {
        initialize();
    }

    /**
     * The getTableDisplayControllerInstance() method returns the instance of this TableViewController to the calling
     * method.
     * @return  The current TableViewController instance as TableViewController.
     */
    public static TableDisplayControllerBackup getTableDisplayControllerInstance() {
        return TableDisplayControllerBackup.tDController;
    }

    /**
     * The editPatientProfile() method allows a user to change their profile data. It opens the editPatientDetails FXML
     * file.
     *
     * @throws IOException  Because we use the FXMLLoader class, we can encounter an IOException.
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
     * The closeTable() method closes the table view and returns the user back to the login screen.
     *
     * @param actionEvent The action event fired by the Sign-Out button.
     * @throws IOException  We use the FXMLLoader class which can throw an IOException.
     */
    public void closeTableView(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent, FXMLUtility.loginFXML);
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
}


