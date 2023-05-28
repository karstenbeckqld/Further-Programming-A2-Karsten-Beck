package com.karstenbeck.fpa2.utilities;

import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * The DatePickerSettings class provides settings for the used DatePicker.
 *
 * @author Karsten Beck
 * @version 1.0 (11/05/2023)
 */
public class DatePickerSettings {

    /**
     * The setWeekends() method returns a Callback to set the weekends of the date picker to have a blue background
     * and white text to make them more prominent.
     *
     * @return  A Callback that sets the format of the days on weekends.
     */
    public static Callback<DatePicker, DateCell> setWeekends() {

        return new Callback<DatePicker, DateCell>() {
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {

                            /* We set the background colour of the weekend days to blue to make them distinguishable. */
                            this.setTextFill(Color.WHITE);
                            this.setStyle("-fx-background-color: #3c7af8");

                            /* Now we define the hover effect for these days, so that the user can see if they select a
                             * weekend day.
                             */
                            this.setOnMouseEntered(e->setStyle("-fx-background-color: #c5c5c5"));
                            this.setOnMouseExited(e->setStyle("-fx-background-color:  #3c7af8"));
                        }
                    }
                };
            }
        };
    }
}
