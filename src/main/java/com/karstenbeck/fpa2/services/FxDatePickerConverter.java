package com.karstenbeck.fpa2.services;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FxDatePickerConverter extends StringConverter<LocalDate> {
    // Default Date Pattern
    private String pattern = "dd/MM/yyyy";
    // The Date Time Converter
    private final DateTimeFormatter dateTimeFormatter;

    public FxDatePickerConverter() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(this.pattern);
    }

   /*  @Override
    public String toString(LocalDate o) {
        return null;
    } */

    public FxDatePickerConverter(String pattern) {
        this.pattern = pattern;
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    // Change String to LocalDate
    @Override
    public LocalDate fromString(String text) {
        LocalDate date = null;

        if (text != null && !text.trim().isEmpty()) {
            date = LocalDate.parse(text, dateTimeFormatter);
        }

        return date;
    }

    // Change LocalDate to String
    @Override
    public String toString(LocalDate date) {
        String text = "";

        if (date != null) {
            text = dateTimeFormatter.format(date);
        }

        return text;
    }
}