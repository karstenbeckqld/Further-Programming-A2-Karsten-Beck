package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DataTransfer;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordSelectorController {


    @FXML
    public Button saveRecords;

    @FXML
    public Button clearSelection;

    @FXML
    public Button selectAll;

    @FXML
    public CheckBox checkSelectAll;

    @FXML
    public Button exit;

    private String patientId;

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
    private TableColumn<PatientRecord, Boolean> selectCol;

    private Stage stage;

    private ObservableList<PatientRecord> patientRecords;

    public void initialize() {

        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();
        System.out.println("Patient ID from Record Selector: " + patientId);

        this.patientRecords = new RecordFinder().where("patientId", this.patientId).getData("records");

        this.tableView.setItems(patientRecords);
        this.tableView.setEditable(true);


        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        this.selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());


        /* this.saveRecords.setOnAction(e -> {
            Object[] checkedItems = this.tableView.getItems().filtered(PatientRecord::getSelected).toArray();         this.tableView.getItems().stream().filter(PatientRecord::getSelected).toArray();

            System.out.printf("There are %,d checked items:%n", checkedItems.length);
            for (var item : checkedItems) {
                System.out.println("  " + item);
            }
        }); */

        this.selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(Integer param) {
                // System.out.println("RecordId " + patientRecords.get(param).getRecordId() + " changed value to " + patientRecords.get(param).getSelected());
                return patientRecords.get(param).selectedProperty();
            }
        }));

        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        this.selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));

        System.out.println("Number of records from Record selector: " + patientRecords.size());

        /* tableView.setOnMouseClicked((MouseEvent event) -> {
            onEdit();
            tableView.setStyle("-fx-background-color: transparent");
        }); */

        /* ObservableList<PatientRecord> test = patientRecords;
        for (int i = 0; i < test.size(); i++) {
            System.out.println("Test values from Patientrecord list.");
            System.out.println("Record ID: " + test.get(i).getRecordId() + " - Comment: " + test.get(i).getComment() + " selected: " + test.get(i).selectedProperty());
            System.out.println();
        } */

        this.checkSelectAll.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            // Loop through entire TableView to set the selected property for each Item
            for (PatientRecord item : this.tableView.getItems()) {
                item.setSelected(newValue);
            }
        }));

        this.saveRecords.setOnAction(e -> {
            try {
                saveSelectedRecords();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setStage(Stage stage) {
        this.exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        this.saveRecords.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    saveSelectedRecords();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Add record saving method here
            }
        });

    }

    private void saveSelectedRecords() throws IOException {

        ArrayList<HashMap<String, String>> selectedRecords = new ArrayList<>();

        for (int i = 0; i < this.patientRecords.size(); i++) {

            HashMap<String, String> record = new HashMap<>();

            if (patientRecords.get(i).selectedProperty().get()) {
                record.put("recordId", patientRecords.get(i).getRecordId());
                record.put("date", patientRecords.get(i).getDate());
                record.put("time", patientRecords.get(i).getTime());
                record.put("weight", patientRecords.get(i).getWeight());
                record.put("temperature", patientRecords.get(i).getTemperature());
                record.put("sysBp", patientRecords.get(i).getSysBp());
                record.put("diaBp", patientRecords.get(i).getDiaBp());
                record.put("comment", patientRecords.get(i).getComment());

            }
            if (record.get("recordId") != null) {
                selectedRecords.add(record);
            }
        }

        DataTransfer.setData(selectedRecords);

        for (int i = 0; i < selectedRecords.size(); i++) {
            System.out.println("Redord ID: " + selectedRecords.get(i).get("recordId")
                    + " - Date: " + selectedRecords.get(i).get("date")
                    + " - Time: " + selectedRecords.get(i).get("time")
                    + " - Weight: " + selectedRecords.get(i).get("weight")
                    + " - Temperature: " + selectedRecords.get(i).get("temperature")
                    + " - sysBp: " + selectedRecords.get(i).get("sysBp")
                    + " - diaBp: " + selectedRecords.get(i).get("diaBp")
                    + " - Comment: " + selectedRecords.get(i).get("comment"));
        }


        Stage saveFileDialogue = new Stage();
        saveFileDialogue.setTitle("Save Records");

        FXMLLoader loader = new FXMLLoader(FXMLUtility.saveFileDialogue);

        Scene scene = new Scene(loader.load());
        saveFileDialogue.setScene(scene);

        SaveFileDialogueController saveFileDialogueController = loader.getController();
        saveFileDialogueController.setData(selectedRecords);
        saveFileDialogueController.setStage(saveFileDialogue);

        saveFileDialogue.show();




    }

    private void clearSelection() {
    }

    private void selectAllRecords() {
    }

    public void onEdit() {
        // check the table's selected item and get selected item
        if (this.tableView.getSelectionModel().getSelectedItem() != null) {
            ArrayList<PatientRecord> data = new ArrayList<>();
            // PatientRecord selectedRecord = this.tableView.getSelectionModel().getSelectedItem();
            data.add(this.tableView.getSelectionModel().getSelectedItem());
           /*  String date = selectedRecord.getDate();
            String time = selectedRecord.getTime(); */
            for (int i = 0; i < data.size(); i++) {
                System.out.println(data.get(i).toString());
            }

        }
    }
}
