package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import Data.Album;
import Data.Photo;
import Data.tag;
import Util.SaveLoad;

public class EditTag extends AppCompatActivity {

    Button btBack;
    Button btSave;
    Button btDelete;
    ToggleButton btToggle;

    EditText txTagValue;


    ArrayList<Album> albums;
    Album selectedAlbum;
    int selectedAlbumIndex;
    int selectedPhotoIndex;
    int selectedTagIndex;
    Photo currPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selectedPhotoIndex = myIntent.getIntExtra("selectedPhoto", -1);
        this.selectedTagIndex = myIntent.getIntExtra("selectedTag", -1);
        selectedAlbum = albums.get(selectedAlbumIndex);
        currPhoto = selectedAlbum.getPhoto(selectedPhotoIndex);

        btBack = findViewById(R.id.btEditBack);
        btBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTag.this, DetailImage.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                intent.putExtra("selectedPhoto", selectedPhotoIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        tag currTag = currPhoto.getTags().get(selectedTagIndex);

        txTagValue = findViewById(R.id.txEditTagValue);
        txTagValue.setText(currTag.getValue());

        btToggle = findViewById(R.id.btEditType);
        if(currTag.getName().toLowerCase().equals("person")){
            btToggle.setChecked(false);
        }else{
            btToggle.setChecked(true);
        }

        btSave = findViewById(R.id.btEditSaveTag);
        btSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasAdded = false;
                boolean isCorr = false;
                String tagName = btToggle.getText().toString();
                String tagValue = txTagValue.getText().toString();
                if(tagValue != null){
                    if(tagValue.length() > 0){
                        isCorr = true;
                    }
                }
                if(isCorr == true) {
                    tag newTag = new tag(tagName, tagValue);
                    boolean dupeExists = false;
                    for(int i = 0; i< currPhoto.getTags().size(); i++) {
                        tag _tag = currPhoto.getTags().get(i);
                        if (_tag.equals(newTag) && i != selectedTagIndex) {
                            dupeExists = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditTag.this);
                            builder.setCancelable(true);
                            builder.setTitle("Duplicate");
                            builder.setMessage("Duplicate album exists");
                            builder.setPositiveButton("OK", null);

                            builder.show();
                        }
                    }
                    if (dupeExists == false) {
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(txTagValue.getWindowToken(), 0);
                        currPhoto.getTags().set(selectedTagIndex, newTag);
                        //currPhoto.addTag(newTag);
                        SaveLoad.saveAlbum(EditTag.this, albums);
                    }
                }
            }
        });

        btDelete = findViewById(R.id.btnDeleteTag);
        btDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTag.this);
                builder.setCancelable(true);
                builder.setTitle("Delete tag");
                builder.setMessage("This will delete the Tag. Continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        currPhoto.getTags().remove(selectedTagIndex);
                        SaveLoad.saveAlbum(EditTag.this, albums);
                        backToActivity();
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }
        });
    }

    private void backToActivity(){
        Intent intent = new Intent(EditTag.this, DetailImage.class);
        intent.putExtra("albums", albums);
        intent.putExtra("selectedAlbum", selectedAlbumIndex);
        intent.putExtra("selectedPhoto", selectedPhotoIndex);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}