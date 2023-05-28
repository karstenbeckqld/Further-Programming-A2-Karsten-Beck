package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;

import java.util.HashMap;

/**
 * The Record class is the parent class for all record classes the application might encounter.
 *
 * @author Karsten Beck
 * @version 2.0 (12/05/2023)
 */
public abstract class Record {

    protected HashMap<String, Object> data;
    protected String table;

    public Record(String table, HashMap<String, Object> row) {
        this.data = row;
        this.table = table;
    }

    /**
     * The saveRecord() method provides a ubiquitous method to save record values to the database.
     * 
     * @return The outcome of saving the data as boolean value: true -> success, false -> failure
     */
    public boolean saveRecord(String patientId){
        return DatabaseConnection.saveRecordData(this.data,patientId);
    }

    /**
     * The deleteRecord() method provides a ubiquitous method to delete records from the database.
     *
     * @param table     The table to select from the database as String
     * @param row       The table row to select as String (also called the key)
     * @param value     The table value to select as String
     * @return          A boolean value indicating success or failure of the process
     */
    public boolean deleteRecord(String table, String row, String value) {

        return DatabaseConnection.booleanQuery("DELETE FROM " + table + " WHERE " + row + "='" + value + "'");
    }

    /**
     * The updateRecord() method uses the DatabaseConnection class to save changes in a record dto the database.
     *
     * @param recordId The ID of the record to update.
     * @return A boolean value indicating success or failure of the process
     */
    public boolean updateRecord (String recordId){
        return DatabaseConnection.updatePatientRecord(this.data, recordId);
    }

    /**
     * The toString() method that overrides the Object toString method and outputs a String representation of the
     * Patient class.
     *
     * @return The instance variables of the Patient class as String
     */
    @Override
    public String toString() {
        return this.data.toString();
    }

}
