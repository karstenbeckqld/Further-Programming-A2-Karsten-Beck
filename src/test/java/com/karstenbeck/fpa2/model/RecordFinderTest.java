package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;

public class RecordFinderTest {

    @Test
    public void testRecordRetrievalFromDatabase() {
        ObservableList<PatientRecord> result = new RecordFinder().getData("patients");
        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testRetrievalOfSpecificRecord() {
        ObservableList<PatientRecord> result = new RecordFinder().where("patientID", "43").getData("records");
        Assert.assertEquals("86", result.get(2).getDiaBp());
    }

    @Test
    public void testAmountOfRecordsReceivedForSpecificPatientId() {
        ObservableList<PatientRecord> result = new RecordFinder().where("patientID", "43").getData("records");
        Assert.assertEquals(13,result.size());
    }
}
