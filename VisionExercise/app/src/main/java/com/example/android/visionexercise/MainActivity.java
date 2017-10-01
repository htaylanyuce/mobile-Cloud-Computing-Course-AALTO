package com.example.android.visionexercise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends Activity {
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    ImageView imageView;
    FaceDetector detector;
    BarcodeDetector barcodeDetector;
    TextView textPic;
    TextView textBarcod;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.btnPickPhoto);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);

                barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();

                detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();

            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                imageView = (ImageView)findViewById(R.id.image);
                imageView.setImageURI(null);
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                //imageView = (ImageView)findViewById(R.id.image);
                imageView.setImageURI(selectedImageUri);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = detector.detect(frame);
                if(faces!= null)
                {
                    textPic = (TextView)findViewById(R.id.txtNumPeople);
                    textPic.setText(faces.size()+"");
                }

                detector.release();

                SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                if(barcodes != null)
                {
                    textBarcod = (TextView)findViewById(R.id.txtBarcode);
                    textBarcod.setText("no");

                }

                barcodeDetector.release();




            }
        }
    }
    public String getPath(Uri uri) {
         if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
         return uri.getPath();
    }

}