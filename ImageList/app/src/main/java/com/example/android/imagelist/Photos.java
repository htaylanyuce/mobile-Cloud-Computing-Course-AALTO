package com.example.android.imagelist;

/**
 * Created by taylan on 23.9.2017.
 */

public class Photos {

    private String mPictureURL;
    private String mAuthor;

    public Photos(String picture, String author)
    {
        mPictureURL = picture;
        mAuthor = author;
    }

    public String getPicture()
    {
        return mPictureURL;
    }
    public String getAuthor()
    {
        return mAuthor;
    }
}