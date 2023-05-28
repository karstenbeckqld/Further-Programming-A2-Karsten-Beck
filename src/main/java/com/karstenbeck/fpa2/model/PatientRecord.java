package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import com.karstenbeck.fpa2.core.MyHealth;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * The PatientRecord class resembles a record a patient can create
 *
 * @author Karsten Beck
 * @version 2.0 (12/05/2023)
 */
public class PatientRecord extends Record {

    /* To be able to change the selected status of a PatientRecord in a tableview, we've added the BooleanProperty
       selected to the class. This allows us to select PatientRecords by ticking a checkbox in the tableview row. */
    private BooleanProperty selected;

    private String setKey;

    private SimpleDateFormat dateFormat;

    /**
     * Non-default constructor which creates an object of the PatientRecord class.
     *
     * @param data The patient's record data as HashMap&lt;String, String&gt;
     */
    public PatientRecord(HashMap<String, Object> data) {
        super("records", data);
        this.selected = new SimpleBooleanProperty(false);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * The specificRecord() method searches for a specific record set in the database.
     *
     * @param key   The key to search for as String
     * @param value The value to search for as String
     * @return A Vector of HashMaps with the result data
     */
    public Vector<HashMap<String, Object>> specificRecord(String key, String value) {
        String sqlQuery = "SELECT * FROM records WHERE '" + key + "' AND '" + value + "'";
        return DatabaseConnection.query(sqlQuery);
    }

    /**
     * The getRecordId() method returns the record ID.
     *
     * @return The record ID as String
     */
    public String getRecordId() {
        return (String) this.data.get("recordId");
    }

    /**
     * The getPatientId() method returns the patient id
     *
     * @return The patient id as String
     */
    public String getPatientId() {
        return (String) this.data.get("patientId");
    }

    /**
     * The getWeight() method returns the weight
     *
     * @return The weight as String
     */
    public String getWeight() {
        return (String) this.data.get("weight");
    }

    /**
     * The getTemperature() method returns the temperature
     *
     * @return The temperature as String
     */
    public String getTemperature() {
        return (String) this.data.get("temperature");
    }

    /**
     * The getSysBp() method returns the systolic blood pressure value
     *
     * @return The systolic blood pressure value as String
     */
    public String getSysBp() {
        return (String) this.data.get("sysBp");
    }

    /**
     * The getDiaBp() method returns the diastolic blood pressure value
     *
     * @return The diastolic blood pressure value as String
     */
    public String getDiaBp() {
        return (String) this.data.get("diaBp");
    }

    /**
     * The getComment() method returns the comment
     *
     * @return The comment as String
     */
    public String getComment() {
        return (String) this.data.get("comment");
    }

    /**
     * The getTime() method returns the local time
     *
     * @return The local time as LocalTime
     */
    public String getTime() {
        return (String) this.data.get("time");
    }

    /**
     * The getDate() method returns the local date
     *
     * @return The local date as LocalDate
     */
    public String getDateAsString() {
        return (String) this.data.get("date");
    }


    public LocalDate getDateAsDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse((String)this.data.get("date"),formatter);
    }

    /**
     * The setSelected() method sets the selected field to the value provided.
     *
     * @param selected A boolean value to set the selected field.
     */
    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    /**
     * The getSelected() method returns the value of the selected field.
     *
     * @return A boolean value of true or false.
     */
    public Boolean getSelected() {
        return this.selected.get();
    }

    /**
     * The selectedProperty() method returns the value of the selected field as BooleanProperty.
     *
     * @return A BooleanProperty representing the state of the selected field.
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public Object getValue() {
        return this.data.get(setKey);
    }

    public void setValue(String value) {
        this.setKey = value;
    }

    /**
     * The getPatientRecordDataSet() method provides the complete record data to the calling method.
     *
     * @return The record data as HashMap&lt;String,String&gt;
     */
    public HashMap<String, Object> getPatientRecordDataSet() {
        return this.data;
    }

    /**
     * The getCurrentPatientRecords() method provides the records for the patient that is currently registered with the
     * MyHealth object.
     *
     * @return A new RecordFinder instance for the records table
     */
    public RecordFinder getCurrentPatientRecords() {
        return new RecordFinder().where("patientId", String.valueOf(MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId()));
    }

    public RecordFinder getPatientDetails(String patUserName, String patPassword) {
        return new RecordFinder().where(patUserName, patPassword);
    }
}
