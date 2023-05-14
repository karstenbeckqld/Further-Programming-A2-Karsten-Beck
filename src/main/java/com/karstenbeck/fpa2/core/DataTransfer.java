package com.karstenbeck.fpa2.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The DataTransfer class is a helper class to copy data from one controller to another.
 *
 * @author Karsten Beck
 * @version 1.0 (13/05/2023)
 */
public class DataTransfer {

    /* The data to be copied as ArrayList of HashMaps<String,String> */
    private static ArrayList<HashMap<String,String>> data;

    /**
     * The setData() method sets the data field in this class to the given values.
     *
     * @param setData   The data to be stored as ArrayList&lt;HashMap&lt;String, String&gt;&gt;
     */
    public static void setData(ArrayList<HashMap<String,String>> setData){
        data = setData;
    }

    /**
     * The getData() returns the data stored in the field data.
     *
     * @return The stored data as ArrayList&lt;HashMap&lt;String, String&gt;&gt;
     */
    public static ArrayList<HashMap<String,String>> getData(){
        return data;
    }
}
