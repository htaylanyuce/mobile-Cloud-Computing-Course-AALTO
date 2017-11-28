package com.example.android.firebaseauthentication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

/**
 * Created by taylan on 16.11.2017.
 */
public class BarcodeCheck extends Activity {

    BarcodeDetector barcodeDetector;
    private File mFile;
    private boolean isBarcode = false;

    public BarcodeCheck(File file)
    {
        barcodeDetector =  new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        mFile = file;

    }

    File image = new File(mFile,"noName" );
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
    SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);


    private void setBarcode()
    {
        if (barcodes.size() > 0) {
            isBarcode = true;

        }
        else
        {
            isBarcode = false;
        }

        barcodeDetector.release();

    }
    public boolean getTypeOfPicture()
    {
        setBarcode();
        return isBarcode;
    }





}