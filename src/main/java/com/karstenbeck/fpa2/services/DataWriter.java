package com.karstenbeck.fpa2.services;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataWriter {

    private ArrayList<HashMap<String, String>> data;
    private String fileName;
    private String directory;
    private static final String DATA_SEPARATOR = ":";

    public DataWriter(ArrayList<HashMap<String, String>> selectedData, String fileName, String directoryName) throws IOException {
        this.data = selectedData;
        this.fileName = fileName;
        this.directory = directoryName;
        writeToFile();
    }

    public void writeToFile() throws IOException {

        FileWriter fw = new FileWriter(this.directory+this.fileName);
        fw.write("recordId,date,time,weight,temperature,sysBp,diaBp,comment\n");
        for (int i=0;i<this.data.size();i++){
            fw.write(this.data.get(i).get("recordId")+",");
            fw.write(this.data.get(i).get("date")+",");
            fw.write(this.data.get(i).get("time")+",");
            fw.write(this.data.get(i).get("weight")+",");
            fw.write(this.data.get(i).get("temperature")+",");
            fw.write(this.data.get(i).get("sysBp")+",");
            fw.write(this.data.get(i).get("diaBp")+",");
            fw.write(this.data.get(i).get("comment")+"\n");
        }
        fw.close();
    }
}
