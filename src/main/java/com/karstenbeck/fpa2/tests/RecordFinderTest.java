package com.karstenbeck.fpa2.tests;

import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RecordFinderTest {

    @Test
    public void testRecordRetrievalFromDatabase() {
        ArrayList<PatientRecord> result = new RecordFinder().getFromDatabase();
        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testRetrievalOfSpecificRecord() {
        ArrayList<PatientRecord> result = new RecordFinder().where("patientID", "1").getFromDatabase();
        Assert.assertEquals("80", result.get(0).getDiaBp());
    }

    @Test
    public void testAmountOfRecordsReceivedForSpecificPatientId() {
        ArrayList<PatientRecord> result = new RecordFinder().where("patientID", "2").getFromDatabase();
        Assert.assertEquals(2,result.size());
    }
}
