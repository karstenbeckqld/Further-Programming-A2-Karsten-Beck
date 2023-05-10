package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.services.DatePickerSettings;
import com.karstenbeck.fpa2.services.FxDatePickerConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AddRecordController extends Controller {


    private HashMap<String, Object> recordData;

    /* Labels */
    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Label sysBpLabel;

    @FXML
    private Label diaBpLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private Label errorMessage;

    /* TextFields */

    @FXML
    private TextField time;

    @FXML
    private TextField weight;

    @FXML
    private TextField temp;

    @FXML
    private TextField sysBp;

    @FXML
    private TextField diaBp;

    @FXML
    private TextArea comment;


    /* Buttons */
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    /* Date Picker */
    @FXML
    public DatePicker datePicker;

    @FXML
    private javafx.scene.layout.GridPane gridPane;

    private String pickedDate;

    private final String pattern = "dd/MM/yyyy";

    private Time setTime;

    public void initialize() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        this.datePicker.setEditable(false);
        this.datePicker.setDayCellFactory(DatePickerSettings.setWeekends());

        this.time.setText(formatter.format(now));
        this.datePicker.getEditor().setText(dateFormatter.format(today));

        this.pickedDate=dateFormatter.format(today);
    }



    public void setStage(Stage stage) {
        this.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        this.confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    confirmEdit();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });

    }

    public void confirmEdit() throws IOException {
        this.recordData = new HashMap<>();
        int errNumber = 0;


        recordData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId());
        recordData.put("date", this.pickedDate);
        recordData.put("time", this.time.getText());
        recordData.put("weight", this.weight.getText());
        recordData.put("temperature", this.temp.getText());
        recordData.put("sysBp", this.sysBp.getText());
        recordData.put("diaBp", this.diaBp.getText());
        recordData.put("comment", this.comment.getText());


        PatientRecord newPatRecord = new PatientRecord(recordData);
        newPatRecord.saveRecord();

        TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
        tDC.reloadTable();

    }

    public static LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public void getDate(ActionEvent actionEvent) {

        LocalDate datePickerDate = this.datePicker.getValue();
        // System.out.println(formattedDate);
        this.pickedDate = datePickerDate.format(DateTimeFormatter.ofPattern(this.pattern));

    }
}