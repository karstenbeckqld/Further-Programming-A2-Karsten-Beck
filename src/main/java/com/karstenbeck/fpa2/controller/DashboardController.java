package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.services.FXMLUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardController {
    @FXML
    private AnchorPane mainContainer;

    @FXML
    private AnchorPane dashParent;

    @FXML
    private AnchorPane sideBarContainer;

    private static DashboardController dashboardController;



    public void initialize() throws IOException {
        DashboardController dashboardController1=this;
        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.menu);
        AnchorPane menuItems = menuLoader.load();
        this.sideBarContainer.getChildren().add(menuItems.getChildren().get(0));
        //this.sideBarContainer.setStyle(MyHealth.class.getResource("/com/karstenbeck/fpa2/css/menu.css").toExternalForm());
        MenuController menuController = menuLoader.getController();

        FXMLLoader overview = new FXMLLoader(FXMLUtility.recordListing);
        AnchorPane nodes = overview.load();

        AnchorPane contentOuterContainer = (AnchorPane) this.dashParent.getChildren().get(0);
        contentOuterContainer.getChildren().addAll(nodes.getChildren());
    }
}
