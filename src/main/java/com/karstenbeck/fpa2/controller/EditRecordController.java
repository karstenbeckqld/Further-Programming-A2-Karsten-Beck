package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.services.DatePickerSettings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * The EditRecordController class handles all activities related to editing a patient's record.
 *
 * @author Karsten Beck
 * @version 1.0 (12/05/2023)
 */
public class EditRecordController extends Controller {

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

        /* Setting up he buttons so that the user can use either the mouse or the keyboard to hit enter.  */
        EventHandler<MouseEvent> mouseClickHandler = mouseEvent -> {
            if (MouseButton.PRIMARY.equals(mouseEvent.getButton())) {
                confirmEdit();
            }
        };

        EventHandler<KeyEvent> pressEnterKeyHandler = keyEvent -> {
            if (keyEvent.getCode() == ENTER) {
                confirmEdit();
            }
        };

        confirmBtn.setOnMousePressed(mouseClickHandler);
        confirmBtn.setOnKeyPressed(pressEnterKeyHandler);

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
            confirmEdit();
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

        /* To be able to use the value from the database we set the pickedDate field to the database value here. If the
           user doesn't change anything, we'll just use this value. */
        this.pickedDate = record.get(0).getDate();

        /* Now we fill the form fields with the data from the database. */
        this.datePicker.getEditor().setText(record.get(0).getDate());
        this.time.setText(record.get(0).getTime());
        this.weight.setText(record.get(0).getWeight());
        this.temp.setText(record.get(0).getTemperature());
        this.sysBp.setText(record.get(0).getSysBp());
        this.diaBp.setText(record.get(0).getDiaBp());
        this.comment.setText(record.get(0).getComment());

    }

    /**
     * The confirmEdit() method writes the field values to the database.
     */
    public void confirmEdit() {

        /* We first initialise the HashMap that'll hold all Record data here. */
        this.patientData = new HashMap<>();

        /* Now we add the data field's content to the HashMap. Because the records table is linked to the patients table
        in the database, we also have to write the patient ID. which we can get in the following line. */
        patientData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId());

        /* Now we add all form field data. */
        patientData.put("date", this.pickedDate);
        patientData.put("time", this.time.getText());
        patientData.put("weight", this.weight.getText());
        patientData.put("temperature", this.temp.getText());
        patientData.put("sysBp", this.sysBp.getText());
        patientData.put("diaBp", this.diaBp.getText());
        patientData.put("comment", this.comment.getText());
        patientData.put("recordId", this.record.get(0).getRecordId());

        /* From the entered data we create a new PatientRecord instance and use the updateRecord() method of the
        Controller superclass to update the patient record in the database. */
        PatientRecord newPatRecord = new PatientRecord(patientData);
        boolean result = newPatRecord.updateRecord();

        /* If the update was successful, we reload the tableview in the TableDisplayController. */
        if (result) {
            TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
            tDC.reloadTable();
        }
    }

    /**
     * The getDate() helper method takes the date from the DatePicker and assigns it to the pickedDate field while
     * formatting the date according to the format pattern defined at the beginning of the class. It is assigned in the
     * FXML file.
     */
    public void getFormattedDate() {

        /* Receive the date from the DatePicker and assign it to the pickedDate field, using the DATE_PATTERN pattern. */
        LocalDate datePickerDate = this.datePicker.getValue();
        this.pickedDate = datePickerDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

}