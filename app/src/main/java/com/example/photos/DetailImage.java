package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import Util.SaveLoad;

public class DetailImage extends AppCompatActivity {

    Button btMove;
    Button btCopy;
    Button btDelete;
//    Button btAddTag;
    Button btCaption;
    Button btBackAlbum;
    //ToggleButton personLocation;

//    ListView tagView;
//    EditText tagValue;
    TextView txCaption;
    ImageView iv;

    private ArrayList<Album> albums;
    //private Album selectedAlbum;
    private int albumIndex;
    private int photoIndex;
    private Photo currPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        //Intent
        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.albumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.photoIndex = myIntent.getIntExtra("selectedPhoto", -1);
        if(photoIndex != -1){
            currPhoto = albums.get(albumIndex).getPhoto(photoIndex);
        }else{
            return;
        }

        btMove = findViewById(R.id.btMove);
        btCopy = findViewById(R.id.btCopy);
        btDelete = findViewById(R.id.btDelete);
//        btAddTag = findViewById(R.id.btnAddTag);
        btCaption = findViewById(R.id.btnChangeCaption);
        btBackAlbum = findViewById(R.id.btBackAlbum);
//        personLocation = findViewById(R.id.togglePerLoc);
//        tagView = findViewById(R.id.tagView);
//        tagValue = findViewById(R.id.txTagValue);
        txCaption = findViewById(R.id.txCaption);
        iv = findViewById(R.id.detailImageView);
        iv.setImageURI(Uri.parse(currPhoto.getImageFile()));
        btMove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btCopy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
//        btAddTag.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        btCaption.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showChangeLangDialog();

            }
        });
        btBackAlbum.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailImage.this, DisplayAlbum.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", albumIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

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

}