package com.karstenbeck.fpa2.core;

import com.karstenbeck.fpa2.model.Patient;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 * The DatabaseConnection class provides functionality for interacting with a SQLite database.
 *
 * @author Karsten Beck
 * @version 2.0 (13/05/2023)
 */
public class DatabaseConnection {

    /* We currently use the absolute path. This will get changed in the final code. */
    /**
     * The JdbcUrl variable stores the path to the database.
     */
    private static final String JdbcUrl = "jdbc:sqlite:/Users/karstenbeck/Documents/Programming/Java/RMIT/FP/FPA2/data.db";

    /**
     * Here we store the required prepared statements as static fields so we can access them in the respective methods.
     */

    private static final String patientStoreStatement = "INSERT INTO patients (firstName, lastName, userName, " +
            "password, email, imageFilePath, photo) VALUES (?,?,?,?,?,?,?)";
    private static final String recordStoreStatement = "INSERT INTO records (patientId, date, time, weight, temperature, " +
            "sysBp, diaBp, comment) VALUES (?,?,?,?,?,?,?,?)";

    private static final String patientRetrieveStatement = "SELECT * FROM patients WHERE patientId=?";
    private static final String recordRetrieveStatement = "SELECT * FROM records WHERE patientId=?";

    private static final String getPatientPhoto = "SELECT photo FROM patients WHERE patientId=?";
    private static final String setPatientPhoto = "UPDATE patients SET photo=? WHERE patientId=?";

    private static final String patientUpdateStatement = "UPDATE patients SET firstName=?, lastName=?, userName=?, " +
            "password=?, email=?, imageFilePath=?, photo=? WHERE patientId=?";

    private static final String updatePasswordStatement ="UPDATE patients SET password=? WHERE patientId=?";


    private static final String recordUpdateStatement = "INSERT INTO records (patientId, date, time, weight, " +
            "temperature, sysBp, diaBp, comment) VALUES (?, ?, ?, ?, ? ,?, ?, ?)";

    private static final String deleteRecordStatement = "DELETE FROM ? WHERE recordId=?";

    /**
     * The savePatientData() method uses a prepared statement to save patient data to the database, including hashing
     * the password and the profile image as BLOB.
     *
     * @param data  The patient data as HashMap of type String, String.
     * @return      A boolean result depicting if the execution was successful or nor.
     */
    public static boolean savePatientData(HashMap<String, String> data) {
        boolean result = false;


        try (Connection connection = DriverManager.getConnection(JdbcUrl);
             Statement statement = connection.createStatement();) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(patientStoreStatement)) {

                preparedStatement.setString(1, data.get("firstName"));
                preparedStatement.setString(2, data.get("lastName"));
                preparedStatement.setString(3, data.get("userName"));
                preparedStatement.setString(4, hashPassword(data.get("password")));
                preparedStatement.setString(5, data.get("email"));
                preparedStatement.setString(6, data.get("imageFilePath"));
                try {
                    FileInputStream fileInputStream = new FileInputStream(data.get("photo"));
                    preparedStatement.setBinaryStream(7, fileInputStream, fileInputStream.available());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                preparedStatement.execute();

                connection.close();

                result = true;
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * The getPatientData() method receives the patient ID value and returns the data for this patient stored in the
     * database.
     *
     * @param patientId The patient ID as String.
     * @return          A HashMap of type String, Object that contains the patient's data, including the image as BLOB.
     */
    public static HashMap<String, Object> getPatientData(String patientId) {

        HashMap<String, Object> data = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(JdbcUrl);
             Statement statement = connection.createStatement();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(patientRetrieveStatement)) {
                preparedStatement.setString(1, patientId);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println(resultSet);
                if (resultSet.next()) {
                    data.put("firstName", resultSet.getString(2));
                    data.put("lastName", resultSet.getString(3));
                    data.put("userName", resultSet.getString(4));
                    data.put("password", resultSet.getString(5));
                    data.put("email", resultSet.getString(6));
                    data.put("imageFilePath", resultSet.getString(7));

                    InputStream imageBlob = resultSet.getBinaryStream(8);
                    Image image = new Image(imageBlob);
                    data.put("photo", image);


                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * The getProfileImage() method loads a patient's profile image from the database as an Image to get displayed in an
     * ImageView container.
     *
     * @param patientId The patient ID as a String.
     * @return            An Image object.
     */
    public static Image getProfileImage(String patientId) {

        Image image = null;

        try (Connection connection = DriverManager.getConnection(JdbcUrl);
             Statement statement = connection.createStatement();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getPatientPhoto)) {
                preparedStatement.setString(1, patientId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    InputStream imageBlob = resultSet.getBinaryStream(1);
                    image = new Image(imageBlob);
                } else {
                    System.out.println("no profile image available.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return image;
    }

    /**
     * The updatePatientDetails() method updates patient details in the database.
     *
     * @param data      A HashMap of type String, String that contains patient data.
     * @param patientId The patient ID to update as String.
     * @return          A boolean value, indicating success or failure of the operation.
     */
    public static boolean updatePatientDetails(HashMap<String, String> data, String patientId) {
        boolean result = false;

        try (Connection connection = DriverManager.getConnection(JdbcUrl);) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(patientUpdateStatement)) {

                preparedStatement.setObject(1, data.get("firstName"));
                preparedStatement.setObject(2, data.get("lastName"));
                preparedStatement.setObject(3, data.get("userName"));
                preparedStatement.setObject(4, hashPassword(data.get("password")));
                preparedStatement.setObject(5, data.get("email"));
                preparedStatement.setObject(6, data.get("imageFilePath"));

                if (data.get("photo") != null) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream((String) data.get("photo"));
                        preparedStatement.setBinaryStream(7, fileInputStream, fileInputStream.available());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                preparedStatement.setString(8,patientId);
                preparedStatement.executeUpdate();
                connection.close();

                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static boolean updatePassword(HashMap<String, String> data, String patientId){
        boolean result = false;

        try (Connection connection = DriverManager.getConnection(JdbcUrl);) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(updatePasswordStatement)) {

                preparedStatement.setObject(1, hashPassword(data.get("password")));
                preparedStatement.setString(2,patientId);
                preparedStatement.executeUpdate();
                connection.close();

                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    /**
     * The query() method runs a query on the database with a given string.
     *
     * @param query The string to run a query for
     * @return The datasets found as Vector&lt;HashMap&lt;String, String&gt;&gt;
     */
    public static Vector<HashMap<String, Object>> query(String query) {

        /* Debugging code */
        // System.out.println("Executing Query: " + query);

        Vector<HashMap<String, Object>> data = new Vector<>();

        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.JdbcUrl);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                HashMap<String, Object> dataSet = new HashMap<>();
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
     * The booleanQuery() method can get used to insert and update patient records in the database.
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

    /**
     * The hashPassword() method hashes a password provided by the user to be stored in the database. It is part of the
     * DatabaseConnection class because here we need to hash passwords to store and to compare passwords.
     *
     * @param password  The password to hash as String.
     * @return          The hashed password as String.
     */
    public static String hashPassword(String password) {
        byte[] hashedPassword = new byte[0];

        try{
            /* For size reasons we use the SHA256 hash algorithm as it produces shorter strings.  */
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hashing failed " + e);
        }

        StringBuilder stringBuilder = new StringBuilder(hashedPassword.length*2);
        for (byte b : hashedPassword) {
            stringBuilder.append(String.format("%x",b));
        }

        return stringBuilder.toString();
    }

}

