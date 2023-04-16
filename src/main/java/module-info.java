module com.karstenbeck.fpa2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;


    opens com.karstenbeck.fpa2 to javafx.fxml;
    exports com.karstenbeck.fpa2.core;
    exports com.karstenbeck.fpa2.controller;
    exports com.karstenbeck.fpa2.model;
    opens com.karstenbeck.fpa2.controller to javafx.fxml;
    opens com.karstenbeck.fpa2.core to javafx.fxml;
    opens com.karstenbeck.fpa2.tests to junit;
    exports com.karstenbeck.fpa2.tests;
}