package com.example.android.firebaseauthentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by taylan on 28.11.2017.
 */

public class TakePhoto extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFireBasePictures;
    private Bundle b;
    private String eventID;
    private String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        b = getIntent().getExtras();
        eventID = b.getString("eventID");
        userID = b.getString("userID");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // file = Uri.fromFile(getOutputMediaFile());
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE & resultCode == RESULT_OK) {
            mFirebaseStorage = FirebaseStorage.getInstance("gs://fir-authentication-482c6.appspot.com");

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            firebase(bitmap);

            Intent i;
            i = new Intent(getApplicationContext(), TakePhoto.class);
            // Pass image index
            i.putExtra("eventID", eventID);
            i.putExtra("userID", userID);

             startActivity(i);

        }
        else if (resultCode == RESULT_CANCELED)
        {
           // Intent intent = new Intent(TakePhoto.this, MainActivity.class);
            // startActivity(intent);
            finish();
        }

    }

    private static File getOutputMediaFile(boolean b) {

        if (b) {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "private");

            //Log.d("external ",myDir+"");
            //String path = Environment.getExternalStorageDirectory().toString();
            //Log.d("path ",path);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            String timeStampl = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File saveFile = new File(myDir.getAbsoluteFile() + File.separator + "IMG" + timeStampl + ".jpg");
            return saveFile;

        } else {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "public");

            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            String timeStampl = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File saveFile = new File(myDir.getAbsoluteFile() + File.separator + "IMG" + timeStampl + ".jpg");
            return saveFile;

        }
    }



    public void firebase(Bitmap bitmap)
    {
        BarcodeDetector barcodeDetector;
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);

        if (barcodes.size() > 0) {
            mFireBasePictures = mFirebaseStorage.getReference().child("private"+"/${"+userID+"}");
            File privateFile = getOutputMediaFile(true);
            Log.d("private ",privateFile.toString());

            try {
                FileOutputStream fos = new FileOutputStream(privateFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                MediaScannerConnection.scanFile(getApplicationContext(), new String[] { privateFile.getPath() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                             }
                        });
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }

            StorageReference picRef = mFireBasePictures.child((Uri.fromFile(privateFile).getLastPathSegment() +""));

            //Toast.makeText(this,"barcode",Toast.LENGTH_SHORT).show();
            picRef.putFile(Uri.fromFile(privateFile));

        } else {

            mFireBasePictures = mFirebaseStorage.getReference().child("public"+"/${"+eventID+"}");
            File publicFile = getOutputMediaFile(false);
            Log.d("public ",publicFile.toString());

            try {
                FileOutputStream fos = new FileOutputStream(publicFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                MediaScannerConnection.scanFile(getApplicationContext(), new String[] { publicFile.getPath() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                             }
                        });
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }

            StorageReference picRef = mFireBasePictures.child((Uri.fromFile(publicFile).getLastPathSegment() +""));


            picRef.putFile(Uri.fromFile(publicFile));

        }

        barcodeDetector.release();
        finish();

    }
}


