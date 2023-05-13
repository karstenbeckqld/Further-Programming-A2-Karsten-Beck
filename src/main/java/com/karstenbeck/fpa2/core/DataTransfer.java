package com.karstenbeck.fpa2.core;

import java.util.ArrayList;
import java.util.HashMap;

public class DataTransfer {

    private static ArrayList<HashMap<String,String>> data;

    public static void setData(ArrayList<HashMap<String,String>> setData){
        data = setData;
    }

    public static ArrayList<HashMap<String,String>> getData(){
        return data;
    }
}
