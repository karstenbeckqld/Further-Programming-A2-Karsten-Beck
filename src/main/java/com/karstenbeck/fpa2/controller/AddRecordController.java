package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.services.DatePickerSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * The AddRecordController class handles all activities related to creating a new PatientRecord
 *
 * @author Karsten Beck
 * @version 1.0 (12/05/2023)
 */
public class AddRecordController extends Controller {


    /* Labels */
    @FXML
    private Label dateLabel, timeLabel, weightLabel, tempLabel, sysBpLabel, diaBpLabel, commentLabel, errorMessage;

    /* TextFields */

    @FXML
    private TextField time, weight, temp, sysBp, diaBp;

    @FXML
    private TextArea comment;


    /* Buttons */
    @FXML
    private Button cancelBtn, confirmBtn;

    /* Date Picker */
    @FXML
    public DatePicker datePicker;

    /* Locally defined fields for class functionality */

    /**
     * The pickedDate field receives the value from the DatePicker to store it in the database.
     */
    private String pickedDate;

    /**
     * The pattern field serves as a central template for the date format to display and to save to the database.
     */
    private final String pattern = "dd/MM/yyyy";

    /**
     * The currentStage field holds the instance of the stage passed to the setStage() method from the calling stage.
     */
    private Stage currentStage;

    /**
     * The initialize() method does housekeeping for the DatePicker and sets date and time formats and assigns them to the
     * required fields.
     */
    public void initialize() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        this.datePicker.setEditable(true);
        this.datePicker.setDayCellFactory(DatePickerSettings.setWeekends());

        this.time.setText(formatter.format(now));
        this.datePicker.getEditor().setText(dateFormatter.format(today));

        this.pickedDate = dateFormatter.format(today);
    }


    /**
     * The setStage() method receives a stage instance from the stage calling this controller and initialises the cancelBtn
     * and confirmBtn event handlers.
     *
     * @param stage The stage for this controller passed as an instance from the calling stage.
     */
    public void setStage(Stage stage) {
        this.currentStage = stage;

        /* When pressing the cancel button, this stage gets closed without action. */
        this.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        /* When pressing the save record button, the confirmEdit() method gets executed and this stage gets closed. */
        this.confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                confirmEdit();
            }
        });

    }

    /**
     * The confirmEdit() method checks if at least one data field had been entered and then saves the PatientRecord to
     * the database.
     */
    public void confirmEdit() {

        /* The HashMap that will hold all entered values for the PatientRecord. Because the PAtientRecord class is a
           child of the Record class, and because the Patient class saves the profile image, the HashMap has been
           altered to contain the String for the key and an Object for the value. Therefore, the PatientRecord class also
           uses the HashMap<String, Object> collection. */
        HashMap<String, Object> recordData = new HashMap<>();

        int numErrors = 0;

        /* Here we check for at least one field to be entered. */
        if (this.weight.getText().isEmpty()
                && this.temp.getText().isEmpty()
                && this.sysBp.getText().isEmpty()
                && this.diaBp.getText().isEmpty()
                && this.comment.getText().isEmpty()) {
            this.errorMessage.setText("First name cannot be empty");
            this.errorMessage.getStyleClass().remove("errorHidden");
            this.weight.getStyleClass().add("textFieldError");
            this.temp.getStyleClass().add("textFieldError");
            this.sysBp.getStyleClass().add("textFieldError");
            this.diaBp.getStyleClass().add("textFieldError");
            this.comment.getStyleClass().add("textFieldError");
            numErrors++;
        } else {
            this.errorMessage.getStyleClass().add("errorHidden");
            this.weight.getStyleClass().remove("textFieldError");
            this.temp.getStyleClass().remove("textFieldError");
            this.sysBp.getStyleClass().remove("textFieldError");
            this.diaBp.getStyleClass().remove("textFieldError");
            this.comment.getStyleClass().remove("textFieldError");
        }

        /* If there are no errors, the data gets collected and stored in the HashMap.  */
        if (numErrors == 0) {
            recordData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId());
            recordData.put("date", this.pickedDate);
            recordData.put("time", this.time.getText());
            recordData.put("weight", this.weight.getText());
            recordData.put("temperature", this.temp.getText());
            recordData.put("sysBp", this.sysBp.getText());
            recordData.put("diaBp", this.diaBp.getText());
            recordData.put("comment", this.comment.getText());


            /* From this HashMap, we create a new PatientRecord and use the saveRecord() method of the Controller class
               to save the record to the database. */
            PatientRecord newPatRecord = new PatientRecord(recordData);
            newPatRecord.saveRecord();

            /* Now we use the TableDisplayController's getTableDisplayControllerInstance method to access the
               TableDisplayController and refresh the tableview.  */
            TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
            tDC.reloadTable();

            this.currentStage.close();
        } else {

            /* If there are errors, a message gets displayed to the user.  */
            this.errorMessage.setText("You must enter at least one data field.");
            this.errorMessage.getStyleClass().remove("errorHidden");
        }

    }

    /**
     * The getDate() method takes the value from the DatePicker and assigns it to the pickedDate field, so that it can
     * get written to the database.
     */
    public void getDate() {
        LocalDate datePickerDate = this.datePicker.getValue();
        this.pickedDate = datePickerDate.format(DateTimeFormatter.ofPattern(this.pattern));
    }
}