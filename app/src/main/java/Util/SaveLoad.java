package Util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import Data.Album;

public class SaveLoad {
    public static ArrayList<Album> loadAlbums(Context context) {
        String fileName = "albums.dat";
        ArrayList<Album> bigList = new ArrayList<Album>();
        boolean fileExists = checkDirExists(context);;


//        File file = new File( context.getFilesDir(),"datadir");
//        if(!file.exists()){
//            dirExists = true;
//            file.mkdir();
//        }else{
//            fileExists = true;
//        }
//
//        try{
//            File gpxfile = new File(file, fileName);
//            if(gpxfile.exists() == true){
//                fileExists = true;
//            }
//            else{
//                FileWriter writer = new FileWriter(gpxfile);
//                writer.append("");
//                writer.flush();
//                writer.close();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//
//        }



        if(fileExists == true) {
            try {

                File outerFile = new File( context.getFilesDir(),"datadir/"+fileName);

                FileInputStream fis = new FileInputStream(outerFile.getPath());
                ObjectInputStream ois = new ObjectInputStream(fis);
                bigList = (ArrayList<Album>) ois.readObject();

                ois.close();
                fis.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bigList;
    }

    public static void saveAlbum(Context context, ArrayList<Album> bigList) {
        String fileName = "albums.dat";
        checkDirExists(context);
        try {

            File outerFile = new File( context.getFilesDir(),"datadir/"+fileName);


            FileOutputStream file = new FileOutputStream(outerFile.getPath());
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(bigList);
//
            out.close();
            file.close();

            // System.out.println("Object has been serialized");
        } catch (Exception ex) {
             System.out.println(ex.getMessage());
        }
    }

    private static boolean checkDirExists(Context context){
        boolean dirExists = false;
        boolean fileExists = false;
        String fileName = "albums.dat";
        File file = new File( context.getFilesDir(),"datadir");
        if(!file.exists()){
            dirExists = true;
            file.mkdir();
        }else{
            fileExists = true;
        }

        try{
            File gpxfile = new File(file, fileName);
            if(gpxfile.exists() == true){
                fileExists = true;
            }
            else{
                FileWriter writer = new FileWriter(gpxfile);
                writer.write("");
                writer.flush();
                writer.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileExists;
    }
}
