package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Data.Album;
import Data.Photo;

public class Slideshow extends AppCompatActivity {

    Button btBack;
    Button btNext;
    Button btPrevious;

    ImageView pictureView;

    TextView txCaption;
    TextView txDate;

    ArrayList<Album>albums;
    Album selectedAlbum;
    int selectedAlbumIndex;
    int selectedPhotoIndex;
    Photo currPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selectedPhotoIndex = myIntent.getIntExtra("selectedPhoto", -1);
        selectedAlbum = albums.get(selectedAlbumIndex);
        currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);

        btBack = findViewById(R.id.btnBackToImage);
        btBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Slideshow.this, DetailImage.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                intent.putExtra("selectedPhoto", selectedPhotoIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        btNext = findViewById(R.id.btnNext);
        btNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(selectedPhotoIndex < selectedAlbum.getLength()-1){
                   selectedPhotoIndex++;
                   currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);
                   updateView();
               }else{
                   AlertDialog.Builder builder = new AlertDialog.Builder(Slideshow.this);
                   builder.setCancelable(true);
                   builder.setTitle("Last image");
                   builder.setMessage("Last image in album");
                   builder.setPositiveButton("OK", null);

                   builder.show();
               }
            }
        });

        btPrevious = findViewById(R.id.btnPrevious);
        btPrevious.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPhotoIndex > 0){
                    selectedPhotoIndex--;
                    currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);
                    updateView();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Slideshow.this);
                    builder.setCancelable(true);
                    builder.setTitle("Last image");
                    builder.setMessage("Last image in album");
                    builder.setPositiveButton("OK", null);

                    builder.show();
                }
            }
        });

        txCaption = findViewById(R.id.txSlideCaption);
        txDate = findViewById(R.id.txSlideDate);
        pictureView = findViewById(R.id.slideShowImageView);

        updateView();
    }

    private void updateView(){
        txCaption.setText(currPhoto.getCaption());
        txDate.setText(currPhoto.getDate());
        pictureView.setImageURI(Uri.parse(currPhoto.getImageFile()));
    }
}