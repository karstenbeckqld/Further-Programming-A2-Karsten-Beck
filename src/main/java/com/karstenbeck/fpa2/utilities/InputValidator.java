package com.karstenbeck.fpa2.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class InputValidator {

    /* The checkEmail() method returns a boolean value to the calling method to verify if the entered email address
    conforms to the usual email pattern. */

    /**
     * The isEmail() method checks if the provided email matches a specific RegEx pattern
     *
     * @param email The email provided by the user as String
     * @return A boolean that depicts if the email matched or didn't match the RegEx
     */
    public static boolean isEmail(String email) {
        return email.matches("^(.+)@(\\S+)$");
    }

    /**
     * The isInteger() method checks if a value is of format integer.
     *
     * @param text A number in string format.
     * @return A boolean value indicating success (true) or failure (false).
     */
    public static boolean isInteger(String text) {

        /* If the value provided is null, we return false. */
        if (text == null) {
            return false;
        }

        /* If no null input got provided, we trim all whitespace. */
        text = text.trim();

        /* If this results in a blank or empty string, we return false. */
        if (text.isBlank() || text.isEmpty()) {
            return false;
        }

        /* Now we try to parse the string to an integer. If this results in an error, we return false. */
        try {
            Integer.parseInt(text);
        } catch (Exception e) {
            return false;
        }

        /* If we reached this step, we return true as all other checks have fallen through. */
        return true;
    }
}
