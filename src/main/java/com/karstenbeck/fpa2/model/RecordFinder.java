package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

/* The RecordFinder class is supposed to provide concatenated search strings like:
 * RecordFinder findRecords = recordFinder.where("weight",80).limit(10);
 * There will be various searches on the database, however, the extent is not yet known, hence only two methods got
 * implemented. */

/**
 * The RecordFinder class forms a collection of search methods for different scenarios involving record data. This
 * class is expandable and the methods can get concatenated.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class RecordFinder {

    /**
     * The where instance variable stores a key/value pair as a HashMap&lt;String, String&gt;
     */
    private final HashMap<String, String> where;

    /**
     * The limit instance variable defines a limit for the search results.
     */
    private int limit;


    /**
     * Non-Default constructor which creates an object of the RecordFinder class.
     */
    public RecordFinder() {
        this.where = new HashMap<>();
        this.limit = 0;
    }

    /**
     * The where() method returns a HashMap&lt;String, String&gt; with a given key/value pair as object of this class.
     *
     * @param key   A given key as String
     * @param value A given value as String
     * @return An object of the RecordFinder class with the where variable set to given key and value
     */
    public RecordFinder where(String key, String value) {
        this.where.put(key, value);
        return this;
    }

    /**
     * The limit() method sets a limit of records to display.
     *
     * @param limit The value to which the record listing should be limited as int
     * @return An object of the RecordFinder class with the limit set
     */
    public RecordFinder limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * The getData() method combines the remaining methods in this class and executes the database search.
     *
     * @return An ObservableList&lt;T&gt; where T can be a Patient or a PatientRecord.
     */
    public <T extends Record> ObservableList<T> getData(String table) {

        ObservableList<T> databaseContent = FXCollections.observableArrayList();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM " + table);

        if (this.where.size()>0) {
            this.where.forEach((key,value) -> {
                sqlQuery.append(" WHERE ").append(key).append("='").append(value).append("'");
            });
        }

        if (this.limit>0) {
            sqlQuery.append(" AND LIMIT =").append(this.limit);
        }

        Vector<HashMap<String,Object>> queryResult = DatabaseConnection.query(sqlQuery.toString());

        /* Run the sqlQuery on the database and add found records to the ArrayList. */
        if (queryResult.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (HashMap<String,Object> n : queryResult) {
                if (table.equals("patients")){
                    databaseContent.add((T) new Patient(n));
                } else if (table.equals("records")) {
                    databaseContent.add((T) new PatientRecord(n));
                }
            }
        }

        return databaseContent;

    }

    /**
     * The getDataAsArrayList() method combines the remaining methods in this class and executes the database search.
     *
     * @return An ArrayList&lt;T&gt; where T can be a Patient or a PatientRecord.
     */
    public <T extends Record> ArrayList<T> getDataAsArrayList(String table) {

        ArrayList<T> databaseContent = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM " + table);

        if (this.where.size()>0) {
            this.where.forEach((key,value) -> {
                sqlQuery.append(" WHERE ").append(key).append("='").append(value).append("'");
            });
        }

        if (this.limit>0) {
            sqlQuery.append(" AND LIMIT =").append(this.limit);
        }

        Vector<HashMap<String,Object>> queryResult = DatabaseConnection.query(sqlQuery.toString());

        /* Run the sqlQuery on the database and add found records to the ArrayList. */
        if (queryResult.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (HashMap<String,Object> n : queryResult) {
                if (table.equals("patients")){
                    databaseContent.add((T) new Patient(n));
                } else if (table.equals("records")) {
                    databaseContent.add((T) new PatientRecord(n));
                }
            }
        }

        return databaseContent;
    }
}
