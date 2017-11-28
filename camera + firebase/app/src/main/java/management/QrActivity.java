package management;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.firebaseauthentication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by juho on 13.11.2017.
 */

public class QrActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView qrCodeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);
        qrCodeTitle = (TextView) findViewById(R.id.qrCodeTitle);

        String qrCodeString = null;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            qrCodeString = bundle.getString("qrCodeString");
        }

        if(qrCodeString == null) {
            qrCodeString = "Hello world!";
        }

        Bitmap qrCode = getQrCodeBitmap(qrCodeString);
        qrCodeImageView.setImageBitmap(qrCode);
    }

    public Bitmap getQrCodeBitmap(String qrCodeString) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeString, BarcodeFormat.QR_CODE, getBitmapWidth(), getBitmapWidth());
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private int getBitmapWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
