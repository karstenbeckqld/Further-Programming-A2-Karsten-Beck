package com.karstenbeck.fpa2.tests;

import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RecordFinderTest {

    @Test
    public void testRecordRetrievalFromDatabase() {
        ObservableList<PatientRecord> result = new RecordFinder().getData("patients");
        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testRetrievalOfSpecificRecord() {
        ObservableList<PatientRecord> result = new RecordFinder().where("patientID", "1").getData("records");
        Assert.assertEquals("80", result.get(0).getDiaBp());
    }

    @Test
    public void testAmountOfRecordsReceivedForSpecificPatientId() {
        ObservableList<PatientRecord> result = new RecordFinder().where("patientID", "2").getData("records");
        Assert.assertEquals(2,result.size());
    }
}
