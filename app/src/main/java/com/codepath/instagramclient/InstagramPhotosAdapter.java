package com.codepath.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    // Constructor
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // Get the data item and display it using the layout XML
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        // Check if a recycled view is used, otherwise a new view has to be inflated.
        if (convertView == null) {
            // Inflate a new view - Arguments: layout, container, and do not attach to parent yet
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // Find the layout XML objects
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        // Set the data in the layout XML objects.
        // Note that the Instagram API call returned a JSON with image URL
        // Use Picasso third-party library to fetch the image itself
        // Also, while waiting for Picasso to fetch the image, clear the image view if it's recycled
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(ivPhoto);
        tvCaption.setText(photo.getCaption());

        // Return the created view
        return convertView;
    }
}
