
package com.karstenbeck.fpa2.services;

//**** PACKAGE IMPORTS ****\\
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.*;
import java.util.Base64;

//**** START EASY IMAGE CLASS ****\\
public class EasyImage implements Serializable {

    //**** CLASS VARIABLES ****\\

    //Width tracks the width of the image in pixels
    private final int width;

    //Height tracks the height of the image in pixels
    private final int height;

    //The data multi dimensional array stores the pixel data.
    private final int[][] data;


    //**** CONSTRUCTOR METHOD ****\\
    //The easy image constructor method will accept an image and break it down using
    //the pixel reader to its individual pixel data.
    public EasyImage(Image image){
        //set our width and height!
        this.width = (int)image.getWidth();
        this.height = (int)image.getHeight();

        //create our data array
        this.data = new int[this.width][this.height];

        //load all the pixel data into the new data array
        PixelReader pixelReader = image.getPixelReader();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.data[i][j] = pixelReader.getArgb(i, j);
            }
        }
    }

    //**** GET IMAGE METHOD ***\\
    //This method will create a new image from the width and high and then
    //write the stored pixel data back out into that image before returning it.
    public Image getImage() {

        //Create a new image with our width and height;
        WritableImage img = new WritableImage(width, height);

        //create our pixel writer from the image declared above
        PixelWriter pixelWriter = img.getPixelWriter();

        //loop over all our pixels using the width and height and write them out
        //via the pixelWriter.setArgb() method.
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelWriter.setArgb(i, j, data[i][j]);
            }
        }

        //Finally return the image.
        return img;
    }

    //**** STATIC SERIALIZE METHOD ***\\
    //This static method will take in an easy image object and serialize it and
    //return the base64 data of that object.
    public static String serialize(EasyImage easyImage){

        //declare our byte array
        byte[] byteArray = new byte[0];

        //open our try catch loop
        try{
            //Create our byte output stream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //create our object output stream and pass it our byte output stream object.
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

            //write our object out
            objectOutput.writeObject(easyImage);

            //load the data from the output stream to byte array.
            byteArray = byteArrayOutputStream.toByteArray();

        } catch (IOException e) {

            //if we have an error throw it
            System.out.println(e.getMessage());
        }

        //finally return the base64 of that byte array.
        return Base64.getEncoder().encodeToString(byteArray);
    }

    //**** STATIC DESERIALIZE METHOD ****\\
    //This method will accept a base64 byte string and then will return
    //the image easy image object from it.
    public static EasyImage deSerialize(String byteString){

        //declare our bytes array and decode the byte string into it.
        byte[] bytes = Base64.getDecoder().decode(byteString);

        //declare our easy image object ready to be returned.
        EasyImage easyImage = null;

        //start our try catch
        try{

            //create our byte array input stream and pass it the bytes array
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            //create our object input stream and pass it the byte array input stream.
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

            //finally set our object to the result of the read object.
            easyImage =  (EasyImage)objectInput.readObject();

        } catch (IOException | ClassNotFoundException e) {

            //if we get an error then print it to console.
            System.out.println(e.getMessage());
        }

        //finally we return our new image
        return easyImage;
    }
}
