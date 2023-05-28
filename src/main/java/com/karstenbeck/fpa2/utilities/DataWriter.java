package com.karstenbeck.fpa2.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The DataWriter class writes data received as ArrayList&lt;HashMap&lt;String, String&gt;&gt; to a file.
 *
 * @author Karsten Beck
 * @version 1.0 (15/04/2023)
 */
public class DataWriter {

    private final ArrayList<HashMap<String, String>> data;
    private final String fileName;
    private final String directory;
    private static final String DATA_SEPARATOR = ",";

    /**
     * Non-default constructor that creates an instance of the DataWriter class.
     *
     * @param selectedData  The data to save as ArrayList&lt;HashMap&lt;String, String&gt;&gt;.
     * @param fileName      The filename provided by the user as String.
     * @param directoryName The directory provided by the user as String.
     * @throws IOException  The file gets written with the FileWriter class which can throw an IOException.
     */
    public DataWriter(ArrayList<HashMap<String, String>> selectedData, String fileName, String directoryName) throws IOException {
        this.data = selectedData;
        this.fileName = fileName;
        this.directory = directoryName;
        writeToFile();
    }

    /**
     * The writeToFile() method writes the file to disk.
     *
     * @throws IOException The FileWriter class used can throw an IOException.
     */
    public void writeToFile() throws IOException {

        /* We declare a new FileWriter object that takes the directory name and filename as argument. */
        FileWriter fw = new FileWriter(this.directory+this.fileName);

        /* Then we first write the headings to the file. */
        fw.write("recordId,date,time,weight,temperature,sysBp,diaBp,comment\n");

        /* Now we can write tthe data that has been transferred to this class through the constructor with a for-each loop. */
        for (HashMap<String, String> dataItems : this.data) {
            fw.write(dataItems.get("recordId") + DATA_SEPARATOR);
            fw.write(dataItems.get("date") + DATA_SEPARATOR);
            fw.write(dataItems.get("time") + DATA_SEPARATOR);
            fw.write(dataItems.get("weight") + DATA_SEPARATOR);
            fw.write(dataItems.get("temperature") + DATA_SEPARATOR);
            fw.write(dataItems.get("sysBp") + DATA_SEPARATOR);
            fw.write(dataItems.get("diaBp") + DATA_SEPARATOR);
            fw.write(dataItems.get("comment") + "\n");
        }

        /* When the data got written, we close the FileWriter. */
        fw.close();
    }
}
