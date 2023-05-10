package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.CheckBoxTableCell;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class RecordSelectorController {


    @FXML
    public Button saveRecords;

    @FXML
    public Button clearSelection;

    @FXML
    public Button selectAll;

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

    public void initialize() {
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        Checkbox checkbox = new Checkbox();

        System.out.println("Patient ID from Record Selector: " + patientId);


        ObservableList<PatientRecord> patientRecords = new RecordFinder().where("patientId", this.patientId).getData("records");


        this.tableView.setItems(patientRecords);

        //this.selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));
        this.selectCol.setCellFactory(p->{
            CheckBox checkBox = new CheckBox();
            TableCell<PatientRecord, Boolean> tableCell = new TableCell<PatientRecord,Boolean>(){
                @Override
                protected void updateItem(Boolean item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty || item == null){
                        setGraphic(null);
                    } else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };

            /* checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event->
                    validate(checkBox, (PatientRecord) tableCell.getTableRow().getItem(), event)); */

            /* checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event->
                    validate(checkBox, (PatientRecord) tableCell.getTableRow().getItem(), event)); */

            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            return tableCell;
        });

        this.selectCol.setEditable(true);
        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.temp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.sysBp.setCellValueFactory(new PropertyValueFactory<>("sysBp"));
        this.diaBp.setCellValueFactory(new PropertyValueFactory<>("diaBp"));
        this.comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        System.out.println("Number of records from Record selector: " + patientRecords.size());

        tableView.setOnMouseClicked((MouseEvent event) -> {

                onEdit();

        });


    }

    private void validate(CheckBox checkBox, PatientRecord item, MouseEvent event){
        // Validate here
        event.consume();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");

        // Set the checkbox if the user want to continue
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            checkBox.setSelected(!checkBox.isSelected());
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
                saveSelectedRecords();
                // Add record saving method here
            }
        });

    }

    private void saveSelectedRecords() {
        System.out.println("Save Selected Records pressed.");
    }

    private void clearSelection(){
    }

    private void selectAllRecords(){
    }

    public void onEdit() {
        // check the table's selected item and get selected item
        if (this.tableView.getSelectionModel().getSelectedItem() != null) {
            PatientRecord selectedRecord = this.tableView.getSelectionModel().getSelectedItem();
            String date = selectedRecord.getDate();
            String time = selectedRecord.getTime();
            System.out.println("Record date: " + date + " - Record time: " + time);
        }
    }
}
