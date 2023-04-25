package com.karstenbeck.fpa2.core;

import java.util.HashMap;
import java.util.Vector;
import java.sql.*;

/**
 * The DatabaseConnection class provides functionality for interacting with a SQLite database.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class DatabaseConnection {

    /* We currently use the absolute path. This will get changed in the final code. */
    /**
     * The JdbcUrl variable stores the path to the database.
     */
    private static final String JdbcUrl = "jdbc:sqlite:/Users/karstenbeck/Documents/Programming/Java/RMIT/FP/FPA2/data.db";

    /**
     * The query() method runs a query on the database with a given string.
     *
     * @param query The string to run a query for
     * @return The datasets found as Vector&lt;HashMap&lt;String, String&gt;&gt;
     */
    public static Vector<HashMap<String, String>> query(String query) {

        /* Debugging code */
        // System.out.println("Executing Query: " + query);

        Vector<HashMap<String, String>> data = new Vector<>();

        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.JdbcUrl);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                HashMap<String, String> dataSet = new HashMap<>();
                int i = 1;
                while (i <= resultSet.getMetaData().getColumnCount()) {
                    dataSet.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                    i++;
                }
                data.add(dataSet);
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return data;
    }

    /**
     * The booleanQuery() method can get used to insert and update records in the database.
     *
     * @param query The query to get run on the database as String
     * @return A boolean value of either true (success) or false (fail)
     */
    public static boolean booleanQuery(String query) {

        boolean result = false;

        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.JdbcUrl);
            Statement statement = connection.createStatement();
            int value = statement.executeUpdate(query);
            if (value > 0) {
                result = true;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            result = false;
        }
        return result;
    }
}
