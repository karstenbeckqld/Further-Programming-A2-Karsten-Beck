package com.karstenbeck.fpa2.services;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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

    /* I'm planning to adjust more settings of the date picker, so this class will be a collection of settings in the
       future.  */

    /**
     * The setWeekends() method returns a Callback to set the weekends of the date picker to have a blue background
     * and white text to make them more prominent.
     *
     * @return  A Callback that sets the format of the days on weekends.
     */
    public static Callback<DatePicker, DateCell> setWeekends() {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                            this.setTextFill(Color.WHITE);
                            this.setStyle("-fx-background-color: #3c7af8;");
                        }
                    }
                };
            }
        };

        return dayCellFactory;
    }
}
