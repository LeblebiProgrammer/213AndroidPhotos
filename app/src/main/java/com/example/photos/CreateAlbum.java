package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.lang.String.*;
import java.util.ArrayList;

import Data.Album;
import Util.GenericFunctions;
import Util.SaveLoad;

public class CreateAlbum extends AppCompatActivity {

    private ArrayList<Album> albums;

    Button btSave;
    Button btCancel;
    EditText txAlbum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("album");

        txAlbum = findViewById(R.id.txAlbumName);

        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String albumStr = txAlbum.getText().toString();
                boolean toSave = false;
                if(albumStr != null){
                    if(albumStr.length() > 0){
                        toSave = true;
                    }
                }
                if(toSave == true) {
                    if (GenericFunctions.alreadyAlbum(albumStr, albums) == true) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAlbum.this);
                        builder.setCancelable(true);
                        builder.setTitle("Duplicate");
                        builder.setMessage("Duplicate album exists");
                        builder.setPositiveButton("OK", null);

                        builder.show();

                    } else {
                        Album _album = new Album(albumStr);
                        albums.add(_album);
                        SaveLoad.saveAlbum(CreateAlbum.this, albums);

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("albums", albums);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                }
            }
        });

        btCancel = findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("albums", albums);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}