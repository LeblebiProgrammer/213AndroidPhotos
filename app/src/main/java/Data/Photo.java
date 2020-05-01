package Data;



import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    transient Bitmap myImage;
    transient Bitmap thumbnail;
    private String caption;
    private Calendar timestamp;
    private ArrayList<tag> tags;
    private String location;

    /**
     * constructor for photo obj
     *
     * @param pictureLocation String
     */
    public Photo(String pictureLocation) {
        //myImage = myPhotoInput;
        caption = "";
        timestamp = Calendar.getInstance();
        timestamp.set(Calendar.MILLISECOND, 0);
        tags = new ArrayList<tag>();

        location = pictureLocation;
    }
    /**
     * constructor for photo obj
     *
     * @param myPhotoInput Image
     * @param pictureLocation String
     */
    public Photo(Bitmap myPhotoInput, String pictureLocation) {
        myImage = myPhotoInput;
        caption = "";
        timestamp = Calendar.getInstance();
        timestamp.set(Calendar.MILLISECOND, 0);
        tags = new ArrayList<tag>();

        location = pictureLocation;
    }

    /**
     * returns image location
     *
     * @return String
     */
    public String getImageFile() {
        return this.location;
    }

    /**
     * gets image from myPhoto
     *
     * @return Image
     */
    public Bitmap getImage() {
        return myImage;
    }

    /**
     * gets string from myPhoto
     *
     * @return String
     */
    public String getCaption() {
        return caption;
    }

    /**
     * gets thumbnail from myPhoto
     *
     * @return Bitmap
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    /**
     * gets string from myPhoto
     *
     * @param b tag
     * @return Boolean
     */
    public Boolean containsTag(tag b) {
        for(tag t:tags) {
            if (t.equals(b))
                return true;
        }
        return false;
    }
    /**
     * gets date string for myPhoto
     *
     * @return String
     */
    public String getDate() {
        return timestamp.getTime().toString();
    }

    /**
     * gets calendar for myPhoto
     *
     * @return Calendar
     */
    public Calendar getDateCheck() {
        return timestamp;
    }

    /**
     * adds tag to tag array
     *
     * @param a tag
     */
    public void addTag(tag a) {
        tags.add(a);
    }

    /**
     * setup the image from load
     *
     * @param img Image
     */
    public void setImage(Bitmap img) {
        this.myImage = img;
        //this.tags = new ArrayList<tag>();
    }

    /**
     * setup the image from load
     *
     * @param bmp Bitmap
     */
    public void setThumbnail(Bitmap bmp){
        this.thumbnail = bmp;
    }

    /**
     * setup the tags from cloning
     *
     * @param tagList ArrayList
     */
    public void setTag(ArrayList<tag> tagList) {
        this.tags = tagList;
    }

    /**
     * removes tag to tag array
     *
     * @param index  int
     */
    public void removeTag(int index) {
        tags.remove(index);
    }

    /**
     * adds tag to tag array
     *
     * @return ArrayList
     */
    public ArrayList<tag> getTags() {
        return tags;
    }

    /**
     * sets caption for specific image
     *
     * @param myCaption String
     */
    public void setCaption(String myCaption) {
        caption = myCaption;
    }

    /**
     * gets cloned Photo
     *
     * @return clone
     * @throws CloneNotSupportedException Photo cant be cloned
     */
    public Photo clone() throws CloneNotSupportedException {
        return (Photo) super.clone();
    }
}
