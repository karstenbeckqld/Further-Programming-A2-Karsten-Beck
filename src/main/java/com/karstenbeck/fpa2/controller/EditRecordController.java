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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class EditRecordController {

    private HashMap<String, Object> patientData;

    private ArrayList<PatientRecord> record;

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

    /* TextFields */
    @FXML
    private TextField date;

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

    @FXML
    private DatePicker datePicker;


    /* Buttons */
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    private String pickedDate;

    private final String pattern = "dd/MM/yyyy";

    public void initialize(){
        this.datePicker.setDayCellFactory(DatePickerSettings.setWeekends());

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
        patientData.put("recordId",this.record.get(0).getRecordId());

        PatientRecord newPatRecord = new PatientRecord(patientData);
        boolean result = newPatRecord.updateRecord();

        if (result) {
            TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
            tDC.reloadTable();
        }
    }

    public void getDate(ActionEvent actionEvent) {

        LocalDate datePickerDate = this.datePicker.getValue();
        //System.out.println(formattedDate);
        this.pickedDate = datePickerDate.format(DateTimeFormatter.ofPattern(this.pattern));

    }

}