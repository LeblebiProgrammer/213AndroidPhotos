package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import Data.Album;
import Data.Photo;
import Util.PermissionClass;
import Util.SaveLoad;

public class DisplayAlbum extends AppCompatActivity {

    ArrayList<Album> albums;
    int selectedAlbumIndex = 0;
    Album selectedAlbum;

    TextView lbAlbum;
    Button btBack;
    Button btUpload;
    ListView imageList;
    FloatingActionButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_album);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("album");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        if(selectedAlbumIndex == -1){
            return;
        }
        selectedAlbum = this.albums.get(selectedAlbumIndex);

        lbAlbum = findViewById(R.id.lbAlbumName);
        String albumText = selectedAlbum.getTitle() + " Album";
        lbAlbum.setText(albumText);



        btBack = findViewById(R.id.btnBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albums);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        btUpload = findViewById(R.id.btnUpload);
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Bring up gallery to select a photo
                    startActivityForResult(intent, 5);
                }
            }});

        imageList = findViewById(R.id.imageList);
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                //System.out.println(position);
                Intent intent = new Intent(DisplayAlbum.this, DetailImage.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                intent.putExtra("selectedPhoto", position);
                startActivityForResult(intent, 7);
            }
        });



        editButton = findViewById(R.id.btnEditAlbum);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayAlbum.this, AlbumSettings.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                startActivityForResult(intent, 10);
            }});




        if(PermissionClass.checkPermissionForReadExtertalStorage(DisplayAlbum.this)) {
            //if(PermissionClass.hasPermissions(DisplayAlbum.this, ))
            updateList();
        }
    }


    private void updateList(){
        CustomImageClass cic = new CustomImageClass(this, selectedAlbum);
        imageList.setAdapter(cic);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            //TODO: action
            ClipData mClipData = data.getClipData();
            boolean hasAdded = false;

            if(mClipData != null) {

                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();

                    selectedAlbum.addPhoto(new Photo(uri.toString()));
                    hasAdded = true;
                }
            }else{
                Uri uri = data.getData();
                if(uri != null){
                    selectedAlbum.addPhoto(new Photo(uri.toString()));
                    hasAdded = true;
                }
            }
            if(hasAdded == true) {
                SaveLoad.saveAlbum(this, albums);
                updateList();
            }

        }
        else if(requestCode == 7){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectedAlbumIndex = data.getIntExtra("selectedAlbum", -1);
            if(selectedAlbumIndex != -1){
                this.selectedAlbum = albums.get(selectedAlbumIndex);
                updateList();
            }
            else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albums);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        else if(requestCode == 10){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectedAlbumIndex = data.getIntExtra("selectedAlbum", -1);
            if(selectedAlbumIndex == -1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albums);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else{
                selectedAlbum = albums.get(selectedAlbumIndex);
                lbAlbum.setText(selectedAlbum.getTitle());
                updateList();
            }
        }
    }




}