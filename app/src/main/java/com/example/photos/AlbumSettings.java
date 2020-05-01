package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import Data.Album;
import Util.SaveLoad;

public class AlbumSettings extends AppCompatActivity {

    Button btnChangeName;
    Button btnDeleteAlbum;
    Button btnBack;
    TextView albumName;

    private ArrayList<Album> albums;
    private int selectedAlbumIndex;

    private Album currAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_settings);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);

        if(selectedAlbumIndex == -1){
            return;
        }


        albumName = findViewById(R.id.lbAlbumNameSettings);
        albumName.setText(currAlbum.getTitle());


        btnChangeName = findViewById(R.id.btnEditAlbum);
        btnChangeName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangDialog();
            }
        });

        btnDeleteAlbum = findViewById(R.id.btnDeleteAlbum);
        btnDeleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumSettings.this);
                builder.setCancelable(true);
                builder.setTitle("Delete Album");
                builder.setMessage("This will delete the album. Continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        albums.remove(selectedAlbumIndex);
                        SaveLoad.saveAlbum(AlbumSettings.this, albums);
                        Intent intent = new Intent(AlbumSettings.this, DisplayAlbum.class);
                        intent.putExtra("albums", albums);
                        intent.putExtra("selectedAlbum", -1);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }
        });

        btnBack = findViewById(R.id.btnBackToAlbum);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumSettings.this, DisplayAlbum.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                setResult(Activity.RESULT_OK, intent);
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
        edt.setText(currAlbum.getTitle());
        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                currAlbum.setTitle(edt.getText().toString());
                albumName.setText(currAlbum.getTitle());
                SaveLoad.saveAlbum(AlbumSettings.this, albums);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }
}