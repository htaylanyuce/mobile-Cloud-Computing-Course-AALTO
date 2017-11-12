package com.example.android.visionexercise;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
                imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageURI(null);
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                //imageView = (ImageView)findViewById(R.id.image);

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = calculateInSampleSize(options, 500,500);
                options.inJustDecodeBounds = false;


                Bitmap smallBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hqimage, options);

                BitmapFactory.decodeResource(getResources(), R.mipmap.hqimage, options);


                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                imageView.setImageBitmap(smallBitmap);

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = detector.detect(frame);

                if (faces != null) {
                    textPic = (TextView) findViewById(R.id.txtNumPeople);
                    textPic.setText(faces.size() + "");
                } else {
                    textPic = (TextView) findViewById(R.id.txtNumPeople);

                    textPic.setText("0");

                }

                detector.release();
                faces = null;

                SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                // Toast.makeText(getApplicationContext(),"asdf"+barcodes.size(),Toast.LENGTH_LONG).show();


                if (barcodes.size() > 0) {
                    textBarcod = (TextView) findViewById(R.id.txtBarcode);
                    textBarcod.setText("Yes");

                } else {
                    textBarcod = (TextView) findViewById(R.id.txtBarcode);

                    textBarcod.setText("No");

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
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}