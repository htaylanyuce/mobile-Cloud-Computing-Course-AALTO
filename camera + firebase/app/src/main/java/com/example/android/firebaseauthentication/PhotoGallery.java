package com.example.android.firebaseauthentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by taylan on 16.11.2017.
 */

public class PhotoGallery extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private ArrayList<String> photoList;
    private GridView photoGrid;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogallery);
        photoGrid = (GridView) findViewById(R.id.gridPhotos);
        photoList = new ArrayList<>();



        StorageReference storageRef = FirebaseStorage.getInstance("gs://fir-authentication-482c6.appspot.com").getReference().child("private/IMG_20171119_221610.jpg");


        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("Firebase ",uri.toString());
                photoList.add(uri.toString());
             }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        photoList.add(("https://c1.staticflickr.com/1/169/396525383_9140cca7c0.jpg"));
        photoList.add("https://c1.staticflickr.com/5/4090/5171453436_92d3b3f287.jpg");
        photoList.add("https://c1.staticflickr.com/3/2557/3943872114_5bab1ed4ae.jpg");
        photoList.add("https://c1.staticflickr.com/5/4088/4952370052_62daf4e03e.jpg");
        //photoList.add(R.mipmap.firehunters);
        //photoList.add(R.mipmap.ic_launcher);

        photoAdapter = new PhotoAdapter(this, R.layout.photoarray, photoList);
        photoGrid.setAdapter(photoAdapter);

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {


                Intent intent = new Intent(PhotoGallery.this, ShowPhoto.class);
                intent.putExtra("position",parent.getItemAtPosition(position).toString());
                Log.d("parent get Item ",photoGrid.getItemAtPosition((position)).toString());
                startActivity(intent);


            }


        });
    }
}

