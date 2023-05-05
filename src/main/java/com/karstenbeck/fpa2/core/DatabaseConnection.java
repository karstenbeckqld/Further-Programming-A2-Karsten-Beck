package com.karstenbeck.fpa2.core;

import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

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

    public static boolean preparedStatementQuery(HashMap<String, String> data) throws IOException {
        boolean result = false;

        String firstName = data.get("firstName");
        String lastName = data.get("lastName");
        String userName = data.get("userName");
        String password = data.get("password");
        String email = data.get("email");
        String photoURL = data.get("img");

        try (Connection connection = DriverManager.getConnection(JdbcUrl);
             Statement statement = connection.createStatement();) {

            // USING PREPARED STATEMENT


            String sql = "INSERT INTO patients (firstName,lastName,userName,password,photo,email)" +
                    " VALUES (?, ?, ?, ?, ? ,?)";

            try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

                InputStream fis = new FileInputStream(photoURL);
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, userName);
                pstmt.setString(4, password);
                pstmt.setBinaryStream(5, (InputStream) fis, (int) photoURL.length());
                pstmt.setString(6, email);
                result = pstmt.execute();

            }

            if (result) {
                System.out.println("Insert into table patients executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Problem happened during record insert.");
        }

        return result;
    }

    public static Image retrieveImage(String patId) {

        Image image = null;
        try (Connection connection = DriverManager.getConnection(JdbcUrl);) {

            String sql = "SELECT photo FROM patients WHERE patientId=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {


                preparedStatement.setString(1, patId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                       /*  byte[] blobBytes = rs.getBytes("photo");
                        System.out.println(blobBytes.length); */
                    Blob blob = rs.getBlob("photo");
                    ByteArrayInputStream in = new ByteArrayInputStream(blob.getBytes(1, (int) blob.length()));
                    /*  ByteArrayInputStream in = new ByteArrayInputStream(blobBytes); */
                    image = new Image(in);
                } else {
                    System.out.println("Entry not found");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public static void updateRecord(HashMap<String, String> data, String table, String iD, String method) {
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(JdbcUrl);
             Statement statement = connection.createStatement();) {



            if (table.equals("patients") && method.equals("set")) {
                String firstName = data.get("firstName");
                String lastName = data.get("lastName");
                String userName = data.get("userName");
                String password = data.get("password");
                String email = data.get("email");
                String photoURL = data.get("img");

                String sql = "INSERT INTO patients (firstName,lastName,userName,password,photo,email)" +
                        " VALUES (?, ?, ?, ?, ? ,?)";

                try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

                    InputStream fis = new FileInputStream(photoURL);
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, userName);
                    pstmt.setString(4, password);
                    pstmt.setBinaryStream(5, (InputStream) fis, (int) photoURL.length());
                    pstmt.setString(6, email);
                    result = pstmt.execute();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if (result) {
                    System.out.println("Insert into table" + table + " executed successfully");
                    System.out.println(result + " row(s) affected");
                }
            }

            if (table.equals("records") && method.equals("set")){

                String patId = data.get("patientId");
                String date = data.get("date");
                String time = data.get("time");
                String weight = data.get("weight");
                String temp = data.get("temperature");
                String sysBp = data.get("sysBp");
                String diaBp = data.get("diaBp");
                String comment = data.get("comment");


                String sql = "INSERT INTO records (patientId, date, time, weight, temperature, sysBp, diaBp, comment)" +
                        " VALUES (?, ?, ?, ?, ? ,?, ?, ?)";

                try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

                    pstmt.setString(1, patId);
                    pstmt.setString(2, date);
                    pstmt.setString(3, time);
                    pstmt.setString(4, weight);
                    pstmt.setString(5, temp);
                    pstmt.setString(6, sysBp);
                    pstmt.setString(7, diaBp);
                    pstmt.setString(8, comment);
                    result = pstmt.execute();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (result) {
                    System.out.println("Insert into table" + table + " executed successfully");
                    System.out.println(result + " row(s) affected");
                }

            }

            if (table.equals("patients") && method.equals("get")) {

            }

            if (table.equals("records") && method.equals("get")) {

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Problem happened during record insert.");
        }
    }

}

