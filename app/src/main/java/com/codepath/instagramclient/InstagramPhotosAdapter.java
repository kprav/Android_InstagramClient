package com.codepath.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static int displayWidth = 0;
    private static int displayHeight = 0;

    // View lookup cache for the view holder pattern
    private static class ViewHolder {
        RoundedImageView rivProfilePic;
        ImageView ivPhoto;
        TextView tvCaption;
        TextView tvUserName;
        TextView tvLocation;
        TextView tvLikes;
        TextView tvCreationTime;
    }

    // Constructor
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        displayWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());
        displayHeight = DeviceDimensionsHelper.getDisplayHeight(getContext());
    }

    // Get the data item and display it using the layout XML
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);

        // Cache object
        ViewHolder viewHolder;

        // Check if a recycled view is used, otherwise a new view has to be inflated.
        if (convertView == null) {
            // Inflate a new view - Arguments: layout, container, and do not attach to parent yet
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

            // Create a new cache and assign the XML objects to it
            viewHolder = new ViewHolder();
            viewHolder.rivProfilePic = (RoundedImageView) convertView.findViewById(R.id.rivProfilePic);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCreationTime = (TextView) convertView.findViewById(R.id.tvCreationTime);

            // Assign the cache to the new view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the data to the View Holder.
        // Note that the Instagram API call returned a JSON with image URL
        // Use Picasso third-party library to fetch the image itself
        // Also, while waiting for Picasso to fetch the image, clear the image view if it's recycled
        viewHolder.rivProfilePic.setImageResource(0);
        viewHolder.ivPhoto.setImageResource(0);
        if (photo.getProfilePicUrl() != null && photo.getProfilePicUrl() != "")
            Picasso.with(getContext()).load(photo.getProfilePicUrl()).placeholder(R.mipmap.ic_launcher).into(viewHolder.rivProfilePic);
        if (photo.getImageUrl() != null && photo.getImageUrl() != "")
            Picasso.with(getContext()).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).resize(displayWidth, 0).into(viewHolder.ivPhoto);
        viewHolder.tvUserName.setText(Html.fromHtml("<b>" + photo.getUserName() + "</b>"));
        viewHolder.tvLocation.setText(photo.getLocation());
        viewHolder.tvCaption.setText(Html.fromHtml("<b> <font color=\"#0D47A1\">" + photo.getUserName() + "</font> </b> &nbsp;" + photo.getCaption()));
        viewHolder.tvLikes.setText(photo.getLikesCount() + " likes");
        viewHolder.tvCreationTime.setText(photo.getCreationTimeStr());

        // Return the created view
        return convertView;
    }
}
