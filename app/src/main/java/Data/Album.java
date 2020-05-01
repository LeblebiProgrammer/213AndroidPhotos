package Data;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    String Title;
    ArrayList<Photo> Pictures;

    /**
     * constructor for album
     *
     * @param newName String
     */
    public Album(String newName) {
        Title = newName;
        Pictures = new ArrayList<Photo>();
    }

    /**
     * gets album length
     *
     * @return int
     */
    public int getLength() {
        int value = 0;
        if(Pictures != null) {
            value = Pictures.size();
        }
        return value;
    }

    /**
     * sets album title
     *
     * @param myNewName String
     */
    public void setTitle(String myNewName) {
        Title = myNewName;
    }

    /**
     * checks equality
     *
     * @param toComp	Album
     * @return Boolean
     */
    public boolean equals(Album toComp) {
        return getTitle().equalsIgnoreCase(toComp.getTitle());
    }

    /**
     * gets album title
     *
     * @return String
     */
    public String getTitle() {
        return Title;
    }

    /**
     * gets the photo
     *
     * @param index int
     * @return String
     */
    public Photo getPhoto(int index) {
        return Pictures.get(index);
    }

    /**
     * gets album data structure
     *
     * @return ArrayList
     */
    public ArrayList<Photo> getAlbum() {
        return Pictures;
    }

    /**
     * sets the photo at specified index
     *
     * @param index int
     * @param a     Photo
     */
    public void setPhoto(int index, Photo a) {
        Pictures.add(index, a);
    }

    /**
     * removes the photo
     *
     * @param index int
     */
    public void removePhoto(int index) {
        Pictures.remove(index);
    }

    /**
     * adds the photo
     *
     * @param a Photo
     */
    public void addPhoto(Photo a) {
        Pictures.add(a);
    }

    /**
     * gets the album string
     *
     *
     * @return String
     */
    public String toString() {
        return "Album Name:  " + Title + "   # of Photos:  " + Pictures.size();
    }
}
