package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.utilities.DatePickerSettings;
import com.karstenbeck.fpa2.utilities.InputValidator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

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
    private DatePicker datePicker;

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

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(pattern);

    private String patientId;

    /**
     * The initialize() method does housekeeping for the DatePicker and sets date and time formats and assigns them to the
     * required fields.
     */
    public void initialize() {

        /* First we get the patient ID as we need this to save the record. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* We define a pattern for the time to get displayed. */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");

        /* We define a pattern for the date to get displayed. */
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        /* To display the current time as default in the time field, we set it here. */
        LocalDateTime now = LocalDateTime.now();

        /* To display the current day in the date picker, we define it here. */
        LocalDate today = LocalDate.now();

        /* We set the date picker to not editable which won't allow the user to enter dates manually. This will reduce
         * errors and saves complicated date comparisons.
         */
        this.datePicker.setEditable(false);

        /* Now we set the date picker to display weekend days in a different colour using the DatePickerSettings class. */
        this.datePicker.setDayCellFactory(DatePickerSettings.setWeekends());

        /* Here we set the datepicker to start with Monday and not Sunday. */
        this.datePicker.setOnShowing(event -> Locale.setDefault(Locale.Category.FORMAT, Locale.UK));

        /* Here we declare a converter for the datepicker to convert the displayed date to the date format used in this
         * application.
         */
        this.datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate==null){
                    return "";
                }
                return DATE_FORMATTER.format(localDate);
            }

            @Override
            public LocalDate fromString(String s) {
                if (s==null || s.trim().isEmpty()){
                    return null;
                }
                return LocalDate.parse(s,DATE_FORMATTER);
            }
        });

        /* This formats the time set in the time text field according to our set format. */
        this.time.setText(formatter.format(now));

        /* Here we display today's date formatted according to our required format. */
        //this.datePicker.getEditor().setText(dateFormatter.format(today));
        this.datePicker.setValue(today);

        /* We want the date that gets saved always to be of a specific format, which we set here. */
        this.pickedDate = dateFormatter.format(today);

        /* To avoid errors in the chart display, we preset all values to zero. */
       /*  this.weight.setText("0");
        this.temp.setText("0");
        this.sysBp.setText("0");
        this.diaBp.setText("0"); */
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
    private void confirmEdit() {

        /* The HashMap that will hold all entered values for the PatientRecord. Because the PatientRecord class is a
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
            this.errorMessage.setText("You need to provide at least one value.");
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

        if (!sysBp.getText().isEmpty()) {
            if (!InputValidator.isInteger(this.sysBp.getText())) {
                this.errorMessage.setText("Blood pressures must be whole numbers.");
                this.errorMessage.getStyleClass().remove("errorHidden");
                this.errorMessage.getStyleClass().add("textFieldError");
                this.sysBp.getStyleClass().add("textFieldError");
                numErrors++;
            } else {
                this.sysBp.getStyleClass().remove("textFieldError");
            }
        }
        if (!diaBp.getText().isEmpty()) {
            if (!InputValidator.isInteger(this.diaBp.getText())) {
                this.errorMessage.setText("Blood pressures must be whole numbers.");
                this.errorMessage.getStyleClass().remove("errorHidden");
                this.errorMessage.getStyleClass().add("textFieldError");
                this.diaBp.getStyleClass().add("textFieldError");
                numErrors++;
            } else {
                this.diaBp.getStyleClass().remove("textFieldError");
            }
        }

        /* If there are no errors, the data gets collected and stored in the HashMap.  */
        if (numErrors == 0) {
            recordData.put("patientId", this.patientId);
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
            newPatRecord.saveRecord(this.patientId);

            /* Now we use the TableViewController's getTableDisplayControllerInstance method to access the
               TableViewController and refresh the tableview.  */
            TableViewController tDC = TableViewController.getTableDisplayControllerInstance();
            tDC.reloadTable();

            this.currentStage.close();
        } else {

            /* If there are errors, a message gets displayed to the user. */
            if (numErrors > 1) {
                this.errorMessage.setText("There are errors in your form.");
                this.errorMessage.getStyleClass().remove("errorHidden");
            }
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