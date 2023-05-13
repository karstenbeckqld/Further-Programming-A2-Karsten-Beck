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
 * The TableDisplayController class displays the records stored for a patient, as well as the patient's name and
 * profile image. .
 *
 * @author Karsten Beck
 * @version 1.0 (13/05/2023)
 */
public class TableDisplayController extends Controller {

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

    private int recordId;

    private static TableDisplayController tDController;

    private ObservableList<Patient> patientDetails;

    String patientId;

    /**
     * The initialize() method retrieves the patient ID for the currently registered patient from the MyHealth class and
     * sets the display name as well as creates the TableView and the ImageView of the profile image. It also adds a
     * patient's records to the TableView.
     */
    @FXML
    public void initialize() {

        /* We often refer back to the TableDisplayController from other classes to reload the table data. Therefore, we
           assign the current TableDisplayController class to a variable to be able to access it.  */
        TableDisplayController.tDController = this;

        /* Here we get the patient ID of the currently logged in patient. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

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
        this.buttons.setCellFactory(param -> new TableCell<PatientRecord, Button>() {

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


    private void deletePatientRecord(PatientRecord patientRecord) {

        Stage stage = (Stage) AnchorPane.getScene().getWindow();

        System.out.println(patientRecord.getRecordId() + "   " + patientRecord.getDate());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText("Are you sure you want to delete this record:\n"
                + "Date        : " + patientRecord.getDate() + "\n"
                + "Time        : " + patientRecord.getTime() + "\n"
                + "Weight      : " + patientRecord.getWeight() + "\n"
                + "Temperature : " + patientRecord.getTemperature() + "\n"
                + "systolic BP : " + patientRecord.getSysBp() + "\n"
                + "diastolic BP: " + patientRecord.getDiaBp());
        alert.setHeaderText("Deleting Patient Record");

        Optional<ButtonType> buttonResult = alert.showAndWait();
        if (buttonResult.isPresent()) {
            if (buttonResult.get() == ButtonType.OK) {
                boolean result = patientRecord.deleteRecord("records", "recordId", patientRecord.getRecordId());
                if (result) {
                    this.reloadTable();
                }
                System.out.println("OK pressed");
            } else if (buttonResult.get() == ButtonType.CANCEL) {
                System.out.println("CANCEL pressed");
            }
        }


    }

    public void reloadTable() {
        initialize();
    }

    public static TableDisplayController getTableDisplayControllerInstance() {
        return TableDisplayController.tDController;
    }

    public void editPatientProfile(ActionEvent actionEvent) throws IOException {

        this.patientDetails = new RecordFinder().where("patientId", this.patientId).getData("patients");

        String firstName = this.patientDetails.get(0).getFirstName();
        String lastName = this.patientDetails.get(0).getLastName();


        Stage editPatientProfile = new Stage();
        editPatientProfile.setTitle("Edit Profile for: " + firstName + " " + lastName);

        FXMLLoader loader = new FXMLLoader(FXMLUtility.editPatientDetails);
        Scene scene = new Scene(loader.load());
        editPatientProfile.setScene(scene);

        EditPatientDetailsController editPatientDetailsController = loader.getController();

        editPatientProfile.show();
    }

    private void centerImage() {
        /* https://stackoverflow.com/questions/32781362/centering-an-image-in-an-imageview */
        Image image = this.profileImage.getImage();
        if (image != null) {
            double width = 0;
            double height = 0;

            double ratioX = this.profileImage.getFitWidth() / image.getWidth();
            double ratioY = this.profileImage.getFitHeight() / image.getHeight();

            double coeff = 0;
            if (ratioX > ratioY) {
                coeff = ratioY;
            } else {
                coeff = ratioX;
            }

            width = image.getWidth() * coeff;
            height = image.getHeight() * coeff;

            this.profileImage.setX((this.profileImage.getFitWidth() - width) / 2);
            this.profileImage.setY((this.profileImage.getFitHeight() - height) / 2);
        }
    }


    public void closeTableView(ActionEvent actionEvent) throws IOException {
        stageForward(actionEvent, FXMLUtility.loginFXML);
    }

    public void selectRecords(ActionEvent actionEvent) throws IOException {
        Stage selectView = new Stage();
        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordSelection);
        Scene scene = new Scene(loader.load());
        selectView.setScene(scene);
        RecordSelectorController recordSelectorController = loader.getController();
        recordSelectorController.setStage(selectView);
        selectView.show();
    }
}


