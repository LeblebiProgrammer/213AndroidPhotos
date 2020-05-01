package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Data.Album;
import Util.GenericFunctions;
import Util.SaveLoad;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ListView albumView;
    ArrayList<Album> albums;

    //private FloatingActionButton addAlbum = findViewById(R.id.btAddAlbum);
    private Button addAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumView = (ListView)findViewById(R.id.listview);
        addAlbum = findViewById(R.id.btAddAlbum);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CreateAlbum.class);
                intent.putExtra("album", albums);
                changeActivity(0, intent);
            }
        });

        albums = SaveLoad.loadAlbums(MainActivity.this);
        updateList();

        albumView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                //System.out.println(position);
                Intent intent = new Intent(MainActivity.this, DisplayAlbum.class);
                intent.putExtra("album", albums);
                intent.putExtra("selectedAlbum", position);
                changeActivity(1, intent);



            }
        });


    }


    private void updateList(){
        ArrayList<String> list = GenericFunctions.AlbumToString(albums);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        albumView.setAdapter(arrayAdapter);
    }

    private void changeActivity(int requestCode, Intent intent){
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
                updateList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }else if(requestCode == 1){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            updateList();
        }else if(requestCode == 10){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            updateList();
        }
    }

}