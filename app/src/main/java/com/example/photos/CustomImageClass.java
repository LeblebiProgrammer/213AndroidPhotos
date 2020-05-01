package com.example.photos;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import Data.Album;
import Data.Photo;
import Util.PermissionClass;


public class CustomImageClass extends ArrayAdapter {


    private Activity context;
    private Album selectedAlbum;


    public CustomImageClass(Activity context, Album selectedAlbum) {
        super(context, R.layout.row_item, selectedAlbum.getAlbum());
        this.context = context;

        this.selectedAlbum = selectedAlbum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);

        TextView caption = (TextView) row.findViewById(R.id.textViewCountry);
        ImageView thumbnail = (ImageView) row.findViewById(R.id.imageViewFlag);

        Photo photo = selectedAlbum.getPhoto(position);
        caption.setText(photo.getCaption());
        //textViewCapital.setText(capitalNames[position]);

        try{
            if(PermissionClass.checkPermissionForReadExtertalStorage(this.context)) {
//                Bitmap bmp = BitmapFactory.decodeFile(photo.getImageFile());
                thumbnail.setImageURI(Uri.parse(photo.getImageFile()));
            }
        }catch(Exception ex){
            System.out.println(ex.toString());
        }


        //thumbnail.setImageBitmap(photo.getThumbnail());
        return  row;
    }
}
