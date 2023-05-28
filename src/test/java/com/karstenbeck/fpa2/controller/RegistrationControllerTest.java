package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.EmptyInputFieldException;
import com.karstenbeck.fpa2.model.Patient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class RegistrationControllerTest extends Application {

    private HashMap<String,Object> data;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/karstenbeck/fpa2/scenes/register.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        try {
            setUp();
        } catch (EmptyInputFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws EmptyInputFieldException {
        this.data = new HashMap<>();
        this.data.put("patientId", "100");
        this.data.put("firstName", "John");
        this.data.put("lastName", "Doe");
        this.data.put("userName", "john_doe");
        this.data.put("password", "secret");
        createUser();
    }

    @Test
    public void createUser() throws EmptyInputFieldException {
        Patient patient = new Patient(this.data);
        boolean isSuccessful = patient.saveRecord("54");
        System.out.println("New patient is: " + patient);
        Assert.assertFalse(isSuccessful);

    }
}