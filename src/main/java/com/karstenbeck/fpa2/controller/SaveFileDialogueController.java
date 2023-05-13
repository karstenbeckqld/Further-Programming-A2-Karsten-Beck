package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DataTransfer;
import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.services.DataWriter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class SaveFileDialogueController extends Controller {

    private static final String FILE_EXTENSION = ".csv";

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private Button cancel;

    @FXML
    private Button choose;

    @FXML
    private Button confirm;

    @FXML
    private TextField fileName;

    @FXML
    private TextField filePath;

    private Stage stage;

    private String directory;

    private ArrayList<HashMap<String, String>> data;

    private String enteredFileName;

    private File chosenDirectory;

    @FXML
    void initialize() {

        this.data = DataTransfer.getData();


            System.out.println("Data records from SaveFileDialogueController");
            for (int i = 0; i < this.data.size(); i++) {
                System.out.println(
                        "Redord ID: " + this.data.get(i).get("recordId")
                                + " - Date: " + this.data.get(i).get("date")
                                + " - Time: " + this.data.get(i).get("time")
                                + " - Weight: " + this.data.get(i).get("weight")
                                + " - Temperature: " + this.data.get(i).get("temperature")
                                + " - sysBp: " + this.data.get(i).get("sysBp")
                                + " - diaBp: " + this.data.get(i).get("diaBp")
                                + " - Comment: " + this.data.get(i).get("comment"));
            }




        this.choose.setOnAction(e -> {
            this.directory = openDirectoryChooser();
            this.filePath.setText(this.directory);
        });

        //this.enteredFileName = "/" + fileName.getText() + FILE_EXTENSION;
        //System.out.println(this.enteredFileName);

    }

    private String openDirectoryChooser() {
        System.out.println("Choose pressed.");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a File Path");
        File selectedDirectory = chooser.showDialog(this.stage);

        System.out.println(selectedDirectory.getAbsolutePath());
        return selectedDirectory.getAbsolutePath();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        this.confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    savePatientRecords();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void savePatientRecords() throws IOException {
        this.enteredFileName = "/" + this.fileName.getText() + FILE_EXTENSION;
        System.out.println(this.enteredFileName);
        DataWriter dw = new DataWriter(this.data, this.enteredFileName, this.directory);

        Stage stage = (Stage) anchorPane.getScene().getWindow();

        Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
        creationConfirmation.initModality(Modality.APPLICATION_MODAL);
        creationConfirmation.initOwner(stage);

        creationConfirmation.setContentText("Your file has been saved to:\n" + this.directory+this.enteredFileName);
        creationConfirmation.setHeaderText("Save to file Confirmation");
        Optional<ButtonType> alertResult = creationConfirmation.showAndWait();
        if (alertResult.isPresent()) {
            if (alertResult.get() == ButtonType.OK) {
                stage.close();
                this.stage.close();
            }
        }



    }

    public void setData(ArrayList<HashMap<String, String>> selectedRecords) {
        this.data = selectedRecords;
    }

}
