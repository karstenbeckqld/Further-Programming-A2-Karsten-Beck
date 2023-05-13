package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.DataTransfer;
import com.karstenbeck.fpa2.services.DataWriter;
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

/**
 * The SaveFileDialogueController class lets the user choose a file path and enter a file name to save the selected
 * records to a file.
 *
 * @author Karsten Beck
 * @version 1.0 (12/05/2023)
 */
public class SaveFileDialogueController extends Controller {

    /* The file extension is hard coded and gets defined here. */
    private static final String FILE_EXTENSION = ".csv";

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private Button cancel, choose, confirm;

    @FXML
    private TextField fileName, filePath;

    private Stage stage;

    private String directory;

    private ArrayList<HashMap<String, String>> data;

    /**
     * The initialize() method retrieves the data to save from the utility class DataTransfer via the static method
     * getData(). It also sets the action event for the choose button, so that the user can choose a directory to save to.
     */
    @FXML
    void initialize() {

        this.data = DataTransfer.getData();

        /* When pressing the choose button, the openDirectoryChooser() method gets called and the chosen directory is
           saved to the global variable directory. We populate the filePath text field here as well with the chosen
           directory. By using a global field for the directory, it's value is available for other methods as well. */
        this.choose.setOnAction(e -> {
            this.directory = openDirectoryChooser();
            this.filePath.setText(this.directory);
        });
    }

    /**
     * The openDirectoryChooser() method opens a directory chooser and returns the chosen directory as string to the
     * calling method.
     * @return
     */
    private String openDirectoryChooser() {
        System.out.println("Choose pressed.");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a File Path");
        File selectedDirectory = chooser.showDialog(this.stage);

        return selectedDirectory.getAbsolutePath();
    }

    /**
     * The setStage() method receives the stage instance for the current Stage from the class calling this controller.
     * This way, we get a reference to the current stage and can perform actions on it.
     *
     * @param stage The instance of the current stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        this.cancel.setOnAction(actionEvent -> stage.close());

        this.confirm.setOnAction(actionEvent -> {
            try {
                savePatientRecords();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * The savePatientRecords() method initialised the saving process of the selected records. It builds the complete
     * file name and passes the data, directory and file name on to the DataWriter class that will create the file.
     *
     * @throws IOException  As the DataWriter class handles file IO, it might throw an IOException.
     */
    private void savePatientRecords() throws IOException {

        /* First we build the filename. Because the directory chooser does not return the trailing forward slash, it
           gets added here. We also add the given file extension, here .csv  */
        String enteredFileName = "/" + this.fileName.getText() + FILE_EXTENSION;

        /* Now we pass on all details to the DataWriter class to write the file.  */
        DataWriter dw = new DataWriter(this.data, enteredFileName, this.directory);

        /* We confirm the creation of the file to the user by displaying an alert, */
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        Alert creationConfirmation = new Alert(Alert.AlertType.INFORMATION);
        creationConfirmation.initModality(Modality.APPLICATION_MODAL);
        creationConfirmation.initOwner(stage);

        creationConfirmation.setContentText("Your file has been saved to:\n" + this.directory+ enteredFileName);
        creationConfirmation.setHeaderText("Save to file Confirmation");
        Optional<ButtonType> alertResult = creationConfirmation.showAndWait();
        if (alertResult.isPresent()) {
            if (alertResult.get() == ButtonType.OK) {
                stage.close();
                this.stage.close();
            }
        }
    }
}
