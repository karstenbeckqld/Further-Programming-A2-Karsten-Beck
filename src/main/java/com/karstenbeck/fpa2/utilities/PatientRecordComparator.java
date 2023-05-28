package com.karstenbeck.fpa2.utilities;

import com.karstenbeck.fpa2.model.PatientRecord;

import java.util.Comparator;

/**
 * The PatientRecordComparator class provides functionality to compare two PatientRecord instances.
 *
 * @author Karsten Beck
 * @version 1.0 (27/05/2023)
 */
public class PatientRecordComparator implements Comparator<PatientRecord> {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(PatientRecord o1, PatientRecord o2) {
        return o1.getDateAsDate().compareTo(o2.getDateAsDate());
    }
}
