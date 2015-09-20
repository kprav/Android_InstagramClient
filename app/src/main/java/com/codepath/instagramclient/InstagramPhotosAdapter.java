package com.codepath.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static int displayWidth = 0;
    private static int displayHeight = 0;

    // Constructor
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        displayWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());
        displayHeight = DeviceDimensionsHelper.getDisplayHeight(getContext());
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
        RoundedImageView rivProfilePic = (RoundedImageView) convertView.findViewById(R.id.rivProfilePic);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCreationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);

        // Set the data in the layout XML objects.
        // Note that the Instagram API call returned a JSON with image URL
        // Use Picasso third-party library to fetch the image itself
        // Also, while waiting for Picasso to fetch the image, clear the image view if it's recycled
        rivProfilePic.setImageResource(0);
        ivPhoto.setImageResource(0);
        if (photo.getProfilePicUrl() != null && photo.getProfilePicUrl() != "")
            Picasso.with(getContext()).load(photo.getProfilePicUrl()).placeholder(R.mipmap.ic_launcher).into(rivProfilePic);
        if (photo.getImageUrl() != null && photo.getImageUrl() != "")
            Picasso.with(getContext()).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).resize(displayWidth, 0).into(ivPhoto);
        tvUserName.setText(Html.fromHtml("<b>" + photo.getUserName() + "</b>"));
        tvLocation.setText(photo.getLocation());
        tvCaption.setText(Html.fromHtml("<b> <font color=\"#0D47A1\">" + photo.getUserName() + "</font> </b> &nbsp;" + photo.getCaption()));
        tvLikes.setText(photo.getLikesCount() + " likes");
        tvCreationTime.setText(photo.getCreationTimeStr());

        DeviceDimensionsHelper d = new DeviceDimensionsHelper();

        // Return the created view
        return convertView;
    }
}
