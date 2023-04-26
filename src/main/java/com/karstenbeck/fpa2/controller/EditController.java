package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class EditController {
    @FXML
    private TextField date;

    @FXML
    private TextField time;

    @FXML
    private TextField weight;

    @FXML
    private TextField temp;

    @FXML
    private TextField diaBp;

    @FXML
    private TextField sysBp;

    @FXML
    private Label dateLabel;

    @FXML
    private Label diaBpLabel;

    @FXML
    private Label sysBpLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;


    @FXML
    public void initialize() throws IOException {

    }

    public void setRecord(String recordId) {
        System.out.println(recordId);
        System.out.println("Record ID in EditController: " + recordId);
        ArrayList<PatientRecord> record = new RecordFinder().where("recordId", recordId).getDataAsArrayList("records");
        System.out.println(record.toString());

        String date = record.get(0).getDate();
        String time = record.get(0).getTime();
        String weight = record.get(0).getWeight();
        String temp = record.get(0).getTemperature();
        String sys = record.get(0).getSysBp();
        String dia = record.get(0).getDiaBp();
        String comment = record.get(0).getComment();
        System.out.println(date + time + weight + temp + sys + dia + comment);
        this.date.setText(date);
    }

    public void setStage(Stage stage) {
        this.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
    }

}