package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditRecordController {

    private HashMap<String, String> patientData;

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


    /* Buttons */
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    public void prefillRecord(String recordId) {
        System.out.println(recordId);
        System.out.println("Record ID in EditRecordController: " + recordId);
        this.record = new RecordFinder().where("recordId", recordId).getDataAsArrayList("records");
        System.out.println(record.toString());

        this.date.setText(record.get(0).getDate());
        this.time.setText(record.get(0).getTime());
        this.weight.setText(record.get(0).getWeight());
        this.temp.setText(record.get(0).getTemperature());
        this.sysBp.setText(record.get(0).getSysBp());
        this.diaBp.setText(record.get(0).getDiaBp());
        this.comment.setText(record.get(0).getComment());

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
        this.patientData = new HashMap<>();
        patientData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getId());
        patientData.put("date", this.date.getText());
        patientData.put("time", this.time.getText());
        patientData.put("weight", this.weight.getText());
        patientData.put("temperature", this.temp.getText());
        patientData.put("sysBp", this.sysBp.getText());
        patientData.put("diaBp", this.diaBp.getText());
        patientData.put("comment", this.comment.getText());
        patientData.put("recordId",this.record.get(0).getRecordId());

        PatientRecord newPatRecord = new PatientRecord(patientData);
        newPatRecord.updateRecord();

        TableDisplayController tDC = TableDisplayController.getTableDisplayControllerInstance();
        tDC.reloadTable();
    }

}