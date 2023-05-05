package com.karstenbeck.fpa2.model;

import com.karstenbeck.fpa2.core.DatabaseConnection;

import java.util.HashMap;
import java.util.Vector;

/**
 * The Patient class stores all information relevant to a patient in the system.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class Patient extends Record {


    public Patient(HashMap<String, String> row) {
        super("patients", row);
    }

    /**
     * The getId() method returns a patient's ID
     *
     * @return The patient ID as String
     */
    public String getId() {
        return this.data.get("patientId");
    }

    /**
     * The getFirstName() method returns a patient's first name
     *
     * @return The fist name as String
     */
    public String getFirstName() {
        return (String) this.data.get("firstName");
    }

    /**
     * The getLastName() method returns a patient's last name
     *
     * @return The last name as String
     */
    public String getLastName() {
        return (String) this.data.get("lastName");
    }

    /**
     * The getUserName() method returns the patient's username.
     *
     * @return The userName as String
     */
    public String getUserName() {
        return this.data.get("userName");
    }

    /**
     * The getPassword() method returns a patient's password
     *
     * @return The password as String
     */
    public String getPassword() {
        return (String) this.data.get("password");
    }

    public String getEmail() {
        return this.data.get("email");
    }

    public String getImage(){
        return this.data.get("photo");
    }

    /**
     * The setFirstName() method assigns a first name
     *
     * @param firstName The first name as String
     */
    public void setFirstName(String firstName) {
        this.data.put("firstName", firstName);
        this.saveRecord();
    }

    /**
     * The setLastName() method assigns a last name
     *
     * @param lastName The last name as String
     */
    public void setLastName(String lastName) {
        this.data.put("lastName", lastName);
        this.saveRecord();
    }

    /**
     * The setPassword() method assigns a password
     *
     * @param pwd The password as String
     */
    public void setPassword(String pwd) {
        this.data.put("password", pwd);
        this.saveRecord();
    }

    /**
     * The setPatientByUserName() method takes a username as parameter and returns the corresponding patient from the
     * database. It then sets the patient variable in the MyHealth main file to this patient, so that a patient's
     * profile can get used by subsequent stages.
     *
     * @param uName The username as String
     * @return The patient details as Patient object
     */
    public static Patient setPatientByUserName(String uName) {
        String sqlQuery = "SELECT * FROM patients WHERE userName='" + uName + "'";

        Vector<HashMap<String, String>> patientData = DatabaseConnection.query(sqlQuery);

        return new Patient(patientData.get(0));
    }
}
