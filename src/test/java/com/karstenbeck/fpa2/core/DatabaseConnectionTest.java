package com.karstenbeck.fpa2.core;

import com.karstenbeck.fpa2.model.Patient;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    private HashMap<String, Object> patientData;
    private HashMap<String, Object> recordData;
    private String patientId;

    @Before
    public void setUp() throws Exception {

        MyHealth app = new MyHealth();
        app.setCurrentPatient(Patient.setPatientByUserName("newUser"));

        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        this.recordData = new HashMap<>();

        this.recordData.put("patientId", this.patientId);
        this.recordData.put("date", "28/05/2023");
        this.recordData.put("time", "08:15");
        this.recordData.put("weight", "140");
        this.recordData.put("temperature", "42.1");
        this.recordData.put("sysBp", "138");
        this.recordData.put("diaBp", "105");
        this.recordData.put("comment","new record");
    }

    @After
    public void tearDown() throws Exception {
       this.recordData.clear();
    }


    @Test
    public void saveRecordData() {

        /* We use a patient without any records and add three records to the database for them. So, when we retrieve
         * the records for given patient, we must get three.
         */
        DatabaseConnection.saveRecordData(this.recordData,this.patientId );
        DatabaseConnection.saveRecordData(this.recordData,this.patientId );
        DatabaseConnection.saveRecordData(this.recordData,this.patientId );

        ObservableList<PatientRecord> records = new RecordFinder().where("patientId",this.patientId).getData("records");

        Assert.assertEquals(3, records.size());

        for (PatientRecord record : records) {
            record.deleteRecord("records", "patientId", this.patientId);
        }
    }
}