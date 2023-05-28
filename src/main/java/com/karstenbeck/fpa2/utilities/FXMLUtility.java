
package com.karstenbeck.fpa2.utilities;


import com.karstenbeck.fpa2.core.MyHealth;

import java.net.URL;

/**
 * The FXMLUtility class provides a central point of URL to the required fxml files for various scenes. The URLs are
 * declared static, so that they can get accessed without instantiating the FXMLUtility class itself.
 *
 * @author Karsten Beck
 * @version 4.0 (28/05/2023)
 */
public class FXMLUtility {

    /* URLs to the respective fxml files as public static variables. */
    public static URL login = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/login.fxml");

    public static URL register = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/register.fxml");

    public static URL recordListing = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/tableView.fxml");

    public static URL editView = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/editRecord_old.fxml");

    public static URL editPatientDetails = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/editPatientDetails.fxml");

    public static URL addRecord = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/addRecord.fxml");

    public static URL recordSelection = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/recordSelector.fxml");

    public static URL saveFileDialogue = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/saveFileDialogue.fxml");

    public static URL dashboard = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/dashboard.fxml");

    public static URL menu = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/menu.fxml");

    public static URL resetPassword = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/passwordReset.fxml");

    public static URL averages = MyHealth.class.getResource("/com/karstenbeck/fpa2/scenes/averageView.fxml");
}
