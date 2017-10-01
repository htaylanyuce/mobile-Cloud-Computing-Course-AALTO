package com.example.android.imagelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by taylan on 23.9.2017.
 */

public class PhotoAdapter extends ArrayAdapter<Photos> {
    public PhotoAdapter(Context context, ArrayList<Photos> photos) {
        super(context, 0, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Photos photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pictures_list, parent, false);
        }

        String url = photo.getPicture();
        ImageView picture = (ImageView) convertView.findViewById(R.id.pic);

        Picasso.with(getContext()).load(url).into(picture);

        TextView author = (TextView) convertView.findViewById(R.id.author);

        author.setText(photo.getAuthor());
        return convertView;
     }
}