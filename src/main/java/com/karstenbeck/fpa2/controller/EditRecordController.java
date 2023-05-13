package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.DatePickerSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The EditRecordController class handles all activities related to editing a patient's record.
 *
 * @author Karsten Beck
 * @version 1.0 (12/05/2023)
 */
public class EditRecordController {

    private HashMap<String, Object> patientData;

    private ArrayList<PatientRecord> record;

    /* Labels */
    @FXML
    private Label dateLabel, timeLabel, weightLabel, tempLabel, sysBpLabel, diaBpLabel, commentLabel;

    /* TextFields */
    @FXML
    private TextField time, weight, temp, sysBp, diaBp;

    @FXML
    private TextArea comment;

    @FXML
    private DatePicker datePicker;


    /* Buttons */
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    private String pickedDate;

    private final static String DATE_PATTERN = "dd/MM/yyyy";

    /**
     * The initialize() method sets the layout format for the date picker.
     */
    public void initialize() {
        this.datePicker.setDayCellFactory(DatePickerSettings.setWeekends());

    }

    /**
     * The setStage() method receives the stage instance for the current Stage from the class calling this controller.
     * This way, we get a reference to the current stage and can perform actions on it.
     *
     * @param stage The instance of the current stage.
     */
    public void setStage(Stage stage) {

        /* When the user clicks the Cancel button, we close this stage. */
        this.cancelBtn.setOnAction(actionEvent -> stage.close());

        /* When the user clicks the OK button, we call the confirmEdit() method and then close the stage. */
        this.confirmBtn.setOnAction(actionEvent -> {
            try {
                confirmEdit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.close();
        });

    }

    /**
     * The prefillRecord() method retrieves record data for the selected record from the database and fill sin the
     * fields with this data. This way, the user doesn't have to memorise the values for the record they want to edit.
     *
     * @param recordId The ID of the selected record as String.
     */
    public void prefillRecord(String recordId) {
        System.out.println(recordId);
        System.out.println("Record ID in EditRecordController: " + recordId);
        this.record = new RecordFinder().where("recordId", recordId).getDataAsArrayList("records");
        System.out.println(record.toString());

        this.pickedDate = record.get(0).getDate();

        this.datePicker.getEditor().setText(record.get(0).getDate());
        this.time.setText(record.get(0).getTime());
        this.weight.setText(record.get(0).getWeight());
        this.temp.setText(record.get(0).getTemperature());
        this.sysBp.setText(record.get(0).getSysBp());
        this.diaBp.setText(record.get(0).getDiaBp());
        this.comment.setText(record.get(0).getComment());

    }


    public void confirmEdit() throws IOException {
        this.patientData = new HashMap<>();
        patientData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId());
        patientData.put("date", this.pickedDate);
        patientData.put("time", this.time.getText());
        patientData.put("weight", this.weight.getText());
        patientData.put("temperature", this.temp.getText());
        patientData.put("sysBp", this.sysBp.getText());
        patientData.put("diaBp", this.diaBp.getText());
        patientData.put("comment", this.comment.getText());
        patientData.put("recordId", this.record.get(0).getRecordId());

        PatientRecord newPatRecord = new PatientRecord(patientData);
        boolean result = newPatRecord.updateRecord();

        if (result) {
            TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
            tDC.reloadTable();
        }
    }

    public void getDate(ActionEvent actionEvent) {

        LocalDate datePickerDate = this.datePicker.getValue();
        // System.out.println(formattedDate);
        this.pickedDate = datePickerDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));

    }

}