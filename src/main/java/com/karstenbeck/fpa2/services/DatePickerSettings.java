package com.karstenbeck.fpa2.services;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DatePickerSettings {

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
