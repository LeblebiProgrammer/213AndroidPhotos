package Util;

import java.util.ArrayList;

import Data.Album;

public class GenericFunctions {

    public static boolean alreadyAlbum(String myTitle, ArrayList<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).equals(new Album(myTitle))) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> AlbumToString (ArrayList<Album> albums){
        ArrayList<String> listStr = new ArrayList<String>();
        for(int i = 0; i< albums.size(); i++){
            listStr.add(albums.get(i).toString());
        }
        return listStr;
    }

}
