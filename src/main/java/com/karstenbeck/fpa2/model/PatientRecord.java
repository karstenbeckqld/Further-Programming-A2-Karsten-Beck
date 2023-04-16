package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;

import java.util.HashMap;
import java.util.Vector;

/**
 * The PatientRecord class resembles a record a patient can create
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class PatientRecord extends Record {

    /**
     * Non-default constructor which creates an object of the PatientRecord class.
     *
     * @param data The patient's record data as HashMap&lt;String, String&gt;
     */
    public PatientRecord(HashMap<String, String> data) {
        super("records", data);

    }

    /**
     * The specificRecord() method searches for a specific record set in the database.
     *
     * @param key   The key to search for as String
     * @param value The value to search for as String
     * @return A Vector of HashMaps with the result data
     */
    public Vector<HashMap<String, String>> specificRecord(String key, String value) {
        String sqlQuery = "SELECT * FROM records WHERE '" + key + "' AND '" + value + "'";
        return DatabaseConnection.query(sqlQuery);
    }

    /**
     * The getRecordId() method returns the record ID.
     *
     * @return The record ID as String
     */
    public String getRecordId() {
        return this.data.get("recordId");
    }

    /**
     * The getPatientId() method returns the patient id
     *
     * @return The patient id as String
     */
    public String getPatientId() {
        return this.data.get("patientId");
    }

    /**
     * The getWeight() method returns the weight
     *
     * @return The weight as String
     */
    public String getWeight() {
        return this.data.get("weight");
    }

    /**
     * The getTemperature() method returns the temperature
     *
     * @return The temperature as String
     */
    public String getTemperature() {
        return this.data.get("temperature");
    }

    /**
     * The getSysBp() method returns the systolic blood pressure value
     *
     * @return The systolic blood pressure value as String
     */
    public String getSysBp() {
        return this.data.get("sysBp");
    }

    /**
     * The getDiaBp() method returns the diastolic blood pressure value
     *
     * @return The diastolic blood pressure value as String
     */
    public String getDiaBp() {
        return this.data.get("diaBp");
    }

    /**
     * The getComment() method returns the comment
     *
     * @return The comment as String
     */
    public String getComment() {
        return this.data.get("comment");
    }

    /**
     * The getTime() method returns the local time
     *
     * @return The local time as LocalTime
     */
    public String getTime() {
    return this.data.get("time");
    }

    /**
     * The getDate() method returns the local date
     *
     * @return The local date as LocalDate
     */
    public String getDate() {
    return this.data.get("date");
    }

    /**
     * The getCurrentPatientRecords() method provides the records for the patient that is currently registered with the
     * MyHealth object.
     *
     * @return A new RecordFinder instance for the records table
     */
    public RecordFinder getCurrentPatientRecords() {
        return new RecordFinder().where("patientId", String.valueOf(MyHealth.getMyHealthInstance().getCurrentPatient().getId()));
    }

}
