package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import Data.Album;
import Data.Photo;
import Data.tag;
import Util.GenericFunctions;
import Util.SaveLoad;

public class ManageTags extends AppCompatActivity {

    Button btBack;
    Button btAddTag;
    ToggleButton btToggle;

    EditText txTagValue;
    ListView tagList;

    ArrayList<Album> albums;
    int selectedAlbumIndex;
    int selectedPhotoIndex;

    Photo currPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tags);

        Intent myIntent = getIntent(); // gets the previously created intent
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectedAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selectedPhotoIndex = myIntent.getIntExtra("selectedPhoto", -1);
        currPhoto = albums.get(selectedAlbumIndex).getPhoto(selectedPhotoIndex);

        tagList = findViewById(R.id.listTags);

        btBack = findViewById(R.id.btnBackTag);
        btBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageTags.this, DetailImage.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectedAlbumIndex);
                intent.putExtra("selectedPhoto", selectedPhotoIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        btToggle = findViewById(R.id.toggleButton);

        btAddTag = findViewById(R.id.btnAddTag);
        btAddTag.setOnClickListener( new View.OnClickListener() {
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
                if(isCorr == true){
                    tag newTag = new tag(tagName, tagValue);
                    boolean dupeExists = false;
                    for (tag _tag : currPhoto.getTags()) {
                        if (_tag.equals(newTag)) {
                            dupeExists = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(ManageTags.this);
                            builder.setCancelable(true);
                            builder.setTitle("Duplicate");
                            builder.setMessage("Duplicate album exists");
                            builder.setPositiveButton("OK", null);

                            builder.show();
                            return;
                        }
                    }
                    if(dupeExists == false){

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(txTagValue.getWindowToken(), 0);
                        currPhoto.addTag(newTag);
                        SaveLoad.saveAlbum(ManageTags.this, albums);
                        updateView();
                    }
                }
            }
        });

        txTagValue = findViewById(R.id.txTagValue);
        updateView();
    }

    private void updateView(){
        txTagValue.setText("");
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<tag>tagArr = currPhoto.getTags();
        for(int i = 0; i< tagArr.size(); i++){
            list.add(tagArr.get(i).toString());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        tagList.setAdapter(arrayAdapter);
    }
}