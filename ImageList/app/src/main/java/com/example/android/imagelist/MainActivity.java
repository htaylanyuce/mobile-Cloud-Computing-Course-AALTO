package com.example.android.imagelist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PhotoAdapter photoAdapter;
    private ListView listView;
    private EditText editText;
    private ArrayList<Photos> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getDataBtn = (Button) findViewById(R.id.btnLoadImg);
        editText = (EditText) findViewById(R.id.txtUrl);

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlAddress = (String) editText.getText().toString();

                //urlAddress = "http://www.mocky.io/v2/59a94ceb100000200c3e0a78";
                new NewsTask().execute(urlAddress);
            }
        });

    }
    private class NewsTask extends AsyncTask<String, Void, List<Photos>> {
        @Override
        protected List<Photos> doInBackground(String... address) {

            ArrayList<Photos> photos = (ArrayList<Photos>) Connection.getData(address[0]);
            return photos;

        }

        @Override
        protected void onPostExecute(List<Photos> photos) {


            if (photos != null && !photos.isEmpty()) {
                photoAdapter = new PhotoAdapter(getBaseContext(),(ArrayList<Photos>) photos);
                listView = (ListView) findViewById(R.id.list_item);
                listView.setAdapter(photoAdapter);

            }


        }
    }
}