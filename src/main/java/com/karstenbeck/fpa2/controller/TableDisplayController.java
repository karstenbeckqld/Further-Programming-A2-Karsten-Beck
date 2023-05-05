package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;

public class TableDisplayController extends Controller {

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
    private TableColumn<PatientRecord, String> date;

    @FXML
    private TableColumn<PatientRecord, String> time;

    @FXML
    private TableColumn<PatientRecord, String> weight;

    @FXML
    private TableColumn<PatientRecord, String> temp;

    @FXML
    private TableColumn<PatientRecord, String> sysBp;

    @FXML
    private TableColumn<PatientRecord, String> diaBp;

    @FXML
    private TableColumn<PatientRecord, String> comment;

    @FXML
    private TableColumn<PatientRecord, Button> buttons;

    private String recordId;

    private static TableDisplayController tDController;

    private ObservableList<Patient> patientDetails;

    @FXML
    public void initialize() {
        TableDisplayController.tDController = this;

        String patientID = MyHealth.getMyHealthInstance().getCurrentPatient().getId();

        System.out.println("Patient ID: " + patientID);


        ObservableList<PatientRecord> patientData = new RecordFinder().where("patientId", patientID).getData("records");
        this.tableView.setItems(patientData);
        this.tableView.setEditable(true);

        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        if (patientData.size()>0) {
            this.recordId = patientData.get(0).getRecordId();
        } else{
            System.out.println("No records available.");
        }

        System.out.println("Number of records: " + patientData.size());


        this.patientDetails = new RecordFinder().where("patientId", patientID).getData("patients");
        System.out.println(this.patientDetails.get(0).getImage());

        //byte[] blobBytes = patientDetails.get(0).getImage().getBytes();
        Blob blob;
        /* try {
            blob = new javax.sql.rowset.serial.SerialBlob(blobBytes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } */
        ByteArrayInputStream in;
       /*  BufferedImage img;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } */
       /*  in = new ByteArrayInputStream(blobBytes);
        Image img = new Image(in);
        this.profileImage.setImage(img); */


       /*  Image imageData = DatabaseConnection.retrieveImage(patientID);
        this.profileImage.setImage(imageData); */


        this.buttons.setCellFactory(param -> new TableCell<PatientRecord, Button>() {
            private final Button editButton = new Button("edit");
            private final Button deleteButton = new Button("delete");

            @Override
            public void updateItem(Button patient, boolean empty) {
                super.updateItem(patient, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {

                    deleteButton.getStyleClass().add("deleteButton");
                    deleteButton.setOnAction(event -> {
                        PatientRecord patientRecord = getTableView().getItems().get(getIndex());
                        deletePatientRecord(patientRecord);
                    });

                    editButton.getStyleClass().add("editButton");
                    editButton.setOnAction(event -> {
                        PatientRecord patientRecord = getTableView().getItems().get(getIndex());
                        try {
                            editPatientRecord(patientRecord);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    HBox buttonDisplay = new HBox(editButton, deleteButton);
                    buttonDisplay.getStyleClass().add("hbox");
                    setGraphic(buttonDisplay);
                }
            }
        });

    }


    private void editPatientRecord(PatientRecord patientRecord) throws IOException {
        System.out.println(patientRecord.getRecordId() + "   " + patientRecord.getWeight());
        String recordId = patientRecord.getRecordId();
        ArrayList<PatientRecord> patientRecordFinder = new RecordFinder().where("recordId", patientRecord.getRecordId()).getDataAsArrayList("records");
        System.out.println(patientRecordFinder.toString());

        Stage editView = new Stage();
        editView.setMinWidth(300);
        editView.setMinHeight(300);
        editView.setTitle("Edit Record number: " + this.recordId);
        FXMLLoader loader = new FXMLLoader(FXMLUtility.editView);
        Scene scene = new Scene(loader.load());
        editView.setScene(scene);
        EditRecordController editRecordController = loader.getController();
        editRecordController.prefillRecord(recordId);
        editRecordController.setStage(editView);
        editView.show();
    }

    @FXML
    private void addPatientRecord(Event event) throws IOException {
        Stage addView = new Stage();
        FXMLLoader loader = new FXMLLoader(FXMLUtility.addRecord);
        Scene scene = new Scene(loader.load());
        addView.setScene(scene);
        AddRecordController addRecordController = loader.getController();
        addRecordController.setStage(addView);
        addView.show();
    }


    private void deletePatientRecord(PatientRecord patientRecord) {
        System.out.println(patientRecord.getRecordId() + "   " + patientRecord.getDate());
    }

    public void reloadTable() {
        initialize();
    }

    public static TableDisplayController getTableDisplayControllerInstance() {
        return TableDisplayController.tDController;
    }

    public void editPatientProfile(ActionEvent actionEvent) throws IOException {

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


}


