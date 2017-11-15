package com.example.android.cameraapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {


    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView photoButton = (ImageView) findViewById(R.id.photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
             }
        });
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        Log.d("root",root);
        root = "/storage";
        File myDir = new File(root + "/folder_name");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        Log.d("mydir",myDir.getAbsolutePath()+"");


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(myDir.getAbsoluteFile() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}