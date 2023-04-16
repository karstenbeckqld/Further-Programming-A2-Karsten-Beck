package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;

import java.util.HashMap;

/**
 * The Record class is the parent class for all record classes the application might encounter.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public abstract class Record {

    protected HashMap<String, String> data;
    protected String table;

    public Record(String table, HashMap<String, String> row) {
        this.data = row;
        this.table = table;
    }

    /**
     * The saveRecord() method provides a ubiquitous method to save record values to the database.
     * 
     * @return The outcome of saving the data as boolean value: true -> success, false -> failure
     */
    public boolean saveRecord() {

        /* Create the base component of a SQL query and pass in the required table parameter. */
        String query = "INSERT INTO " + table + " ";
        StringBuilder dataFields = new StringBuilder();
        StringBuilder valueFields = new StringBuilder();
        
        /* Define a StringBuilder object that gets populated with the data fields of the object calling the method. */
        dataFields.append("(");
        data.forEach((key, value) -> {

            dataFields.append(key).append(",");
        });

        /* We have to remove the last character as here is one comma too many. */
        dataFields.deleteCharAt(dataFields.length() - 1);
        dataFields.append(")");

        /* Define a StringBuilder object that gets populated with the values of the object calling the method. */
        valueFields.append(" VALUES ('");
        data.forEach((key, value) -> {

            valueFields.append(value).append("','");
        });

        /* Here we have to remove the last twi characters from the StringBuilder object as there is a apostrophe and
        comma too many. */
        valueFields.deleteCharAt(valueFields.length() - 1);
        valueFields.deleteCharAt(valueFields.length() - 1);
        valueFields.append(");");

        /* Printing out the query for debugging purposes. */
        System.out.println(query + dataFields + valueFields);

        /* Executing the query on the database */
        return DatabaseConnection.booleanQuery(query + dataFields + valueFields);
    }

    /**
     * The deleteRecord() method provides a ubiquitous method to delete records from the database.
     *
     * @param table     The table to select from the database as String
     * @param column    The table column to select as String (also called the key)
     * @param value     The table value to select as String
     * @return          A boolean value indicating success or failure of the process
     */
    public boolean deleteRecord(String table, String column, String value) {

        return DatabaseConnection.booleanQuery("DELETE FROM " + table + " WHERE " + column + "='" + value + "'");
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
