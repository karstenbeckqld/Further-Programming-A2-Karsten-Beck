package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
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

public class AddRecordController {

    private HashMap<String, String> recordData;

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
        recordData.put("patientId", MyHealth.getMyHealthInstance().getCurrentPatient().getId());
        recordData.put("date", this.date.getText());
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

}