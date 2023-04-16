package com.karstenbeck.fpa2.core;

/**
 * Custom exception class that gets thrown when the user doesn't enter data in at least one field.
 *
 * @author      Karsten Beck
 * @version     1.0 (15/04/2023)
 */
public class EmptyInputFieldException extends Throwable {

    /**
     * Non-default constructor creating an object of the EmptyInputFieldException class.
     *
     * @param message   The message to be output as a String.
     */
    public EmptyInputFieldException(String message) {
        super(message);
    }
}
