package com.example.android.firebaseauthentication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by taylan on 16.11.2017.
 */

public class TakePhoto extends AppCompatActivity {

    private ArrayList<File> files;
    private Camera mCamera;
    private CameraPreview mPreview;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFireBasePictures;
    static Button captureButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);



        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        files =new ArrayList<File>();

        // Add a listener to the Capture button
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);


        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
     }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
           // files.add(pictureFile);


            for(int i = 0; i < files.size();i++)
            {
                Log.d("aa",files.get(i)+"");
            }
            if (pictureFile == null){
                 return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                // thread starts
                fos.write(data);
                fos.close();

                Bitmap bm = BitmapFactory.decodeFile(pictureFile.toString());

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

                fos = new FileOutputStream(pictureFile);
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            } catch (FileNotFoundException e) {
             } catch (IOException e) {
             }
              new ProTask().execute(pictureFile);


        }

    };




    @Override
    protected void onPause() {
        super.onPause();
         releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;

        }

    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        captureButton.setVisibility(View.INVISIBLE);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    // Thread

    private class ProTask extends AsyncTask<File, Void,Boolean> {


        @Override
        protected Boolean doInBackground(File... file) {
            // barcode check

            mFirebaseStorage = FirebaseStorage.getInstance();
            mFireBasePictures = mFirebaseStorage.getReference().child("pictures");

           // File image = new File(file[0]);
            //Log.d("image:",Uri.fromFile(image)+"");
            //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            StorageReference picRef = mFireBasePictures.child(Uri.fromFile(file[0]).getLastPathSegment() +"");
            picRef.putFile(Uri.fromFile(file[0]));

            return true;

        }

        @Override
        protected void onPostExecute(Boolean isBarcode) {

            if(isBarcode)
            {

                Toast.makeText(getApplicationContext(),"YES",Toast.LENGTH_LONG).show();
                //captureButton.setVisibility(View.VISIBLE);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
            }

        }
    }
}