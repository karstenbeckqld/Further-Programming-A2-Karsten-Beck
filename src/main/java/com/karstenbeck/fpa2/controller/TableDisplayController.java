package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public void initialize() {
        String patientID = MyHealth.getMyHealthInstance().getCurrentPatient().getId();

        System.out.println("Patient ID: " + patientID);

        this.tableView = new TableView<>();

        ObservableList<PatientRecord> patientData = new RecordFinder().where("patientId", patientID).getData("records");
        System.out.println("Number of patient records: " + patientData.size());

        this.date.setCellValueFactory(new PropertyValueFactory<>(patientData.get(0).getDate()));

        this.tableView.getColumns().addAll(this.date);


    }

}
