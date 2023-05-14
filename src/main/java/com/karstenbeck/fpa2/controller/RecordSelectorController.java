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

/**
 * The RecordSelectorController class lets the user select records from the list to get exported to a file.
 *
 * @author Karsten Beck
 * @version 1.0 (13/05/2023)
 */
public class RecordSelectorController extends Controller {


    @FXML
    public Button saveRecords, exit;

    @FXML
    public CheckBox checkSelectAll;

    @FXML
    private TableView<PatientRecord> tableView;

    @FXML
    private TableColumn<PatientRecord, String> date, time, weight, temp, sysBp, diaBp, comment;

    @FXML
    private TableColumn<PatientRecord, Boolean> selectCol;

    private ObservableList<PatientRecord> patientRecords;

    /**
     * The initialize() method retrieves the patient ID from the MyHealth class to retrieve the records for this patient
     * from the database as an ObservableList. It then fills the tableview with these records. We then create a colum
     * with checkboxes, so that the user can select individual records for export.
     */
    public void initialize() {

        /* Get the patient ID from MyHealth */
        String patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* Retrieve all records from the database for this patient ID. */
        this.patientRecords = new RecordFinder().where("patientId", patientId).getData("records");

        /* Populate the TableView with the records. */
        this.tableView.setItems(patientRecords);

        /* Set the table view to editable. This is important to be able to tick the checkboxes. */
        this.tableView.setEditable(true);

        /* Set the table view so that multiple rows can get selected together. */
        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /* Insert checkboxes for the given table column.  */
        this.selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        /* Checkboxes manipulate BooleanProperties (https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/cell/CheckBoxTableCell.html).
           Therefore, we observe this boolean property here (selectedProperty) to be able to get the state and determine
           which records are selected and which are not.    */
        this.selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());

        /* Here we set the CellValueFactories for the remaining cells.  */
        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        this.selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));

        /* We use a select all checkbox to toggle between selecting and deselecting all checkboxes. The following code
           block enables this functionality.  */
        this.checkSelectAll.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            // Loop through entire TableView to set the selected property for each Item
            for (PatientRecord item : this.tableView.getItems()) {
                item.setSelected(newValue);
            }
        }));
    }

    /**
     * The setStage() method receives the stage instance for the current Stage from the class calling this controller.
     * This way, we get a reference to the current stage and can perform actions on it.
     *
     * @param stage The instance of the current stage.
     */
    public void setStage(Stage stage) {
        this.exit.setOnAction(actionEvent -> stage.close());

        this.saveRecords.setOnAction(actionEvent -> {
            try {
                saveSelectedRecords();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * The saveSelectedRecords() method creates an ArrayList of HashMaps of the selected records and passes it on to a
     * helper class to be transferred to the SaveFileDialogueController class.
     *
     * @throws IOException  Because we're using the FXMLLoader class, the method can throw an IOException.
     */
    private void saveSelectedRecords() throws IOException {

        /* Initialise a new ArrayList of HashMaps to store the records. */
        ArrayList<HashMap<String, String>> selectedRecords = new ArrayList<>();

        /* We first save a selected record in a HashMap and then save the HashMap to the ArrayList. */
        for (int i = 0; i < this.patientRecords.size(); i++) {

            /* Initialise a HashMap to store the record details. */
            HashMap<String, String> record = new HashMap<>();

            /* If the record's boolean property is set to true, we add its details to the HashMap. */
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

            /* Because we're looping through the whole number of PatientRecords, but not all records might be selected,
               we only add the ones that have a recordId value not null. */
            if (record.get("recordId") != null) {
                selectedRecords.add(record);
            }
        }

        /* Now we use the static setData() method from the DataTransfer class to move the data to the next controller. */
        DataTransfer.setData(selectedRecords);

        /* Here we call the SaveFileDialogueController and open a new stage to display the dialogue to save te file. */
        Stage saveFileDialogue = new Stage();
        saveFileDialogue.setTitle("Save Records");

        FXMLLoader loader = new FXMLLoader(FXMLUtility.saveFileDialogue);

        Scene scene = new Scene(loader.load());
        saveFileDialogue.setScene(scene);

        SaveFileDialogueController saveFileDialogueController = loader.getController();
        saveFileDialogueController.setStage(saveFileDialogue);

        saveFileDialogue.show();
    }
}
