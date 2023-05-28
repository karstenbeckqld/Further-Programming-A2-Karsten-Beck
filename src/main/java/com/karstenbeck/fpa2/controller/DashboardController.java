package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.utilities.FXMLUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The DashboardController class displays the main user interface.
 *
 * @author Karsten Beck
 * @version 1.0 (20/05/2023)
 */
public class DashboardController {

    /**
     * The parent pane for the main content.
     */
    @FXML
    private AnchorPane dashParent;

    /**
     * The pane for the sidebar container displaying the menu.
     */
    @FXML
    private AnchorPane sideBarContainer;

    /**
     * The initialize() method creates the dashboard and defines the menu and content sections.
     *
     * @throws IOException The FXMLLoader class can throw an IOException.
     */
    public void initialize() throws IOException {

        /* Here we load the menu from the menu.fxml file. */
        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.menu);

        /* We define an AnchorPane to load the menu into. */
        AnchorPane menuItems = menuLoader.load();

        /* Now we add the menuItems AnchorPane to the sidebar as menu. */
        this.sideBarContainer.getChildren().add(menuItems.getChildren().get(0));

        /* Here we add the stylesheet for the menu. */
        this.sideBarContainer.getStylesheets().add("/com/karstenbeck/fpa2/css/menu.css");

        /* Finally, we load the menu controller. */
        MenuController menuController = menuLoader.getController();

        /* For the main container we load the record listing tableview from the fxml file. */
        FXMLLoader overview = new FXMLLoader(FXMLUtility.recordListing);

        /* Then we load the fxml file into an AnchorPane. */
        AnchorPane nodes = overview.load();

        /* We now have to get the node from the scene that addresses the main window, which happens to be at position 0 */
        AnchorPane contentContainer = (AnchorPane) this.dashParent.getChildren().get(0);

        /* Now we add the scene we've loaded into the AnchorPane to this container. */
        contentContainer.getChildren().addAll(nodes.getChildren());

        /* Finally, we add the stylesheet for this scene here. */
        contentContainer.getStylesheets().add("/com/karstenbeck/fpa2/css/tableView.css");
    }

}
