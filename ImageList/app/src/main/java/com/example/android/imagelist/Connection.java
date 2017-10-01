package com.example.android.imagelist;

/**
 * Created by taylan on 23.9.2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class Connection {


    private static URL url;


    public Connection()
    {

    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        Log.d("URL",url+"");

        return url;
    }

    public static List<Photos> getData(String sURL) {
        url = createUrl(sURL);
        String result = null;

        try {
            result = downloadData(url);
        } catch (IOException e) {

        }

        return exctractFeatures(result);

    }

    private static List<Photos> exctractFeatures(String result){

        ArrayList<Photos> photosArrayList;
        Log.d("resultinExtract",result+"");

        if(result == null)
        {
            return null;
        }

        try {
            JSONArray obj = new JSONArray(result);
            Log.d("obj",obj+"");
             photosArrayList = new ArrayList<Photos>();

            for(int i = 0 ; i < obj.length();i++)
            {
                JSONObject src = obj.getJSONObject(i);
                String photo = src.getString("photo");
                String author = src.getString("author");
               // Log.v("nuin","sddaads");
                //Log.d("photo",photo+"");
               // Log.d("author",author+"");

                Photos n = new Photos(photo,author);

                photosArrayList.add(n);
            }

        }
        catch (JSONException j)
        {
            return null;
        }

        for(int i = 0; i<photosArrayList.size();i++)
        {
            Log.v("author",photosArrayList.get(i).getAuthor());
            Log.v("picture",photosArrayList.get(i).getPicture());

        }
        return photosArrayList;

    }



    private static  String downloadData(URL url) throws IOException {

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String result = null;
        if (url==null)
        {
            return result;
        }

        // prepare the connection
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            // make the connection

            connection.connect();


            int responseCode = connection.getResponseCode();

            // is the connection successful

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            Log.d("response",responseCode+"");

            // convert input stream to string

            inputStream = connection.getInputStream();

            if (inputStream != null) {
                // Converts Stream to String with max length of 500.
                result = toString(inputStream);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d("result",result+"");

        return result;
    }

    // input stream to string converter

    private static String toString(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.d("output",output.toString()+"");

        return output.toString();
    }
}