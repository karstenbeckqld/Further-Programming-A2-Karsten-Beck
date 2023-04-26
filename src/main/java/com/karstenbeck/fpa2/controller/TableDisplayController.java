package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class TableDisplayController extends Controller {

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

    @FXML
    public void initialize() {
        String patientID = MyHealth.getMyHealthInstance().getCurrentPatient().getId();

        System.out.println("Patient ID: " + patientID);



        ObservableList<PatientRecord> patientData = new RecordFinder().where("patientId", patientID).getData("records");
        System.out.println("Number of patient records: " + patientData.size());

        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

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
                        PatientRecord getPatient = getTableView().getItems().get(getIndex());
                       deletePatientRecord(getPatient);
                    });

                    editButton.getStyleClass().add("editButton");
                    editButton.setOnAction(event -> {
                        PatientRecord getPatient = getTableView().getItems().get(getIndex());
                        try {
                            editPatientRecord(getPatient);
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
        this.tableView.setItems(patientData);
    }



    private void editPatientRecord(PatientRecord patientRecord) throws IOException {
        System.out.println(patientRecord.getRecordId() + "   " + patientRecord.getWeight());
        String recordId = patientRecord.getRecordId();
        ArrayList<PatientRecord> patientRecordFinder = new RecordFinder().where("recordId", patientRecord.getRecordId()).getDataAsArrayList("records");
        System.out.println(patientRecordFinder.toString());
        Stage editView = new Stage();
        editView.setMinWidth(300);
        editView.setMinHeight(300);
        editView.setTitle("Edit Record for:");
        FXMLLoader loader = new FXMLLoader(FXMLUtility.editView);
        Scene scene = new Scene(loader.load());
        editView.setScene(scene);
        EditController editController = loader.getController();
        editController.setRecord(recordId);
        editController.setStage(editView);
        editView.show();
    }

    private void deletePatientRecord(PatientRecord patientRecord){
        System.out.println(patientRecord.getRecordId() + "   " + patientRecord.getDate());
    }
}


