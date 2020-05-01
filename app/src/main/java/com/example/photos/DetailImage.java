package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import Data.Album;
import Data.Photo;
import Data.tag;
import Util.SaveLoad;

public class DetailImage extends AppCompatActivity {

    Button btMove;
    Button btCopy;
    Button btDelete;
//    Button btAddTag;
    Button btSlideshow;
    Button btCaption;
    Button btBackAlbum;
    //ToggleButton personLocation;

//    ListView tagView;
//    EditText tagValue;
    TextView txCaption;
    TextView txDate;
    ImageView iv;

    private ArrayList<Album> albums;
    private Album selectedAlbum;
    private int selectedAlbumIndex;
    private int selectedPhotoIndex;
    private Photo currPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        //Intent
        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selectedPhotoIndex = myIntent.getIntExtra("selectedPhoto", -1);

        if(selectedPhotoIndex != -1){
            this.selectedAlbum = albums.get(selectedAlbumIndex);
            currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);
        }else{
            return;
        }

        txCaption = findViewById(R.id.txCaption);

        txDate = findViewById(R.id.txDateView);

        iv = findViewById(R.id.detailImageView);

        btMove = findViewById(R.id.btMove);
        btMove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAlertView(false);
            }
        });

        btCopy = findViewById(R.id.btCopy);
        btCopy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAlertView(true);
            }
        });

        btDelete = findViewById(R.id.btDelete);
        btDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailImage.this);
                builder.setCancelable(true);
                builder.setTitle("Delete Album");
                builder.setMessage("This will delete the Photo. Continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        selectedAlbum.removePhoto(selectedPhotoIndex);
                        SaveLoad.saveAlbum(DetailImage.this, albums);
                        backToActivity(false);
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }

        });

        btCaption = findViewById(R.id.btnChangeCaption);
        btCaption.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showChangeLangDialog();

            }
        });

        btBackAlbum = findViewById(R.id.btBackAlbum);
        btBackAlbum.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailImage.this, DisplayAlbum.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        btSlideshow = findViewById(R.id.btSlideShow);
        btSlideshow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailImage.this, Slideshow.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                intent.putExtra("selectedPhoto", selectedPhotoIndex);
                startActivityForResult(intent, 11);
            }
        });
        updateView();
    }




    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editAlert);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                currPhoto.setCaption(edt.getText().toString());
                txCaption.setText(edt.getText().toString());
                SaveLoad.saveAlbum(DetailImage.this, albums);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void ListAlertView(boolean moveOrCopy){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailImage.this);
        builder.setTitle("Choose an animal");

// add a list
        String[] otherTitles = new String[albums.size()];
        boolean []otherBool = new boolean[albums.size()];
        for (int i = 0; i < albums.size(); i++) {
            otherTitles[i] = albums.get(i).getTitle();
            otherBool[i] = false;
        }
        int moveLocation = 0;

        if(moveOrCopy == true) {
            builder.setMultiChoiceItems(otherTitles, otherBool, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // user checked or unchecked a box
                    otherBool[which] = isChecked;
                }
            });
        }else{
            otherBool[moveLocation] = true;
            builder.setSingleChoiceItems(otherTitles, moveLocation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user checked an item
                    otherBool[moveLocation] = false;
                    otherBool[which] = true;
                }
            });
        }
// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean backHome = false;
                boolean toSave = false;
                for(int i = 0; i< otherBool.length; i++){
                    if(otherBool[i] == true){
                        Photo photo = selectedAlbum.getPhoto(selectedPhotoIndex);
                        if(moveOrCopy == false){
                            Album chosenAlbum = albums.get(i);
                            if(i != selectedAlbumIndex) {
                                chosenAlbum.addPhoto(photo);
                                selectedAlbum.removePhoto(selectedPhotoIndex);
                                backHome = true;
                                toSave = true;
                                break;
                            }
                        }
                        else {
                            //tocopy
                            Album chosenAlbum = albums.get(i);
                            toSave = true;
                            try {
                                Photo clonedPhoto = photo.clone();
                                ArrayList<tag> clonedTag = (ArrayList<tag>) photo.getTags().clone();
                                clonedPhoto.setTag(clonedTag);
                                chosenAlbum.addPhoto(clonedPhoto);

                            }catch (Exception ex){
                                System.out.println(ex.toString());
                            }

                        }
                    }
                }
                if(toSave == true){
                    SaveLoad.saveAlbum(DetailImage.this, albums);
                }
                if(backHome == true){
                    backToActivity(true);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToActivity(boolean backToHome){
        Intent intent;
        if(backToHome == false) {
            intent = new Intent(DetailImage.this, DisplayAlbum.class);
            intent.putExtra("albums", albums);
            intent.putExtra("selectedAlbum", selectedAlbumIndex);
            setResult(Activity.RESULT_OK, intent);

        }else{
            intent = new Intent(DetailImage.this, DisplayAlbum.class);
            intent.putExtra("albums", albums);
            intent.putExtra("selectedAlbum", -1);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 11){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectedAlbumIndex = data.getIntExtra("selectedAlbum", -1);
            this.selectedPhotoIndex = data.getIntExtra("selectedPhoto",-1);
            currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);
            updateView();
        }
    }

    private void updateView(){
        txCaption.setText(currPhoto.getCaption());
        txDate.setText(currPhoto.getDate());
        iv.setImageURI(Uri.parse(currPhoto.getImageFile()));
    }
}