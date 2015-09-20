package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "9b631e2ab4c3481280e3c74c4025a1c3";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter adapterPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // List of photos from Instagram
        photos = new ArrayList<InstagramPhoto>();

        // Attach the custom adapter
        adapterPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(adapterPhotos);

        // Get Instagram photos
        fetchPopularPhotos();
    }

    // Trigger the API request to Instagram and fetch the photos
    public void fetchPopularPhotos() {
        // Construct Instagram API
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Construct the AsyncHttpClient third-party library object to fire off the API request
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Successful network call will return a JSON
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        // Ignore videos, we are only showing popular photos
                        String type = null;
                        if (photoJSON.optString("type") != null) {
                            type = photoJSON.getString("type");
                        }
                        if (type != null && !type.equalsIgnoreCase("image"))
                            continue;

                        String userName = null;
                        String profilePicUrl = null;
                        String caption = null;
                        String imageUrl = null;
                        String location = null;
                        String creationTimeStr = null;
                        long creationTime = 0;
                        int imageWidth = 0;
                        int imageHeight = 0;
                        int likesCount = 0;

                        // Get image attributes from JSON
                        if (photoJSON.optJSONObject("user") != null) {
                            if (photoJSON.getJSONObject("user").optString("username") != null) {
                                userName = photoJSON.getJSONObject("user").getString("username");
                            }
                            if (photoJSON.getJSONObject("user").optString("profile_picture") != null) {
                                profilePicUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                            }
                        }
                        if (photoJSON.optJSONObject("caption") != null) {
                            if (photoJSON.getJSONObject("caption").optString("text") != null) {
                                caption = photoJSON.getJSONObject("caption").getString("text");
                            }
                        }
                        if (photoJSON.optJSONObject("location") != null) {
                            if (photoJSON.getString("location") != null && !photoJSON.getString("location").equalsIgnoreCase("null")) {
                                if (photoJSON.getJSONObject("location").optString("name") != null)
                                location =photoJSON.getJSONObject("location").getString("name");
                            }
                        }
                        if (photoJSON.optJSONObject("images") != null) {
                            if (photoJSON.getJSONObject("images").optJSONObject("standard_resolution") != null) {
                                if (photoJSON.getJSONObject("images").getJSONObject("standard_resolution").optString("url") != null) {
                                    imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                                }
                                if (photoJSON.getJSONObject("images").getJSONObject("standard_resolution").optInt("width") != 0) {
                                    imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                                }
                                if (photoJSON.getJSONObject("images").getJSONObject("standard_resolution").optInt("height") != 0) {
                                    imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                                }
                            }
                        }
                        if (photoJSON.optLong("created_time") != 0) {
                            creationTime = photoJSON.getLong("created_time");
                        }
                        if (photoJSON.optJSONObject("likes") != null) {
                            if (photoJSON.getJSONObject("likes").optInt("count") != 0) {
                                likesCount = photoJSON.getJSONObject("likes").getInt("count");
                            }
                        }

                        // Convert creationTime to relative time
                        creationTimeStr = findRelativeCreationTime(creationTime);

                        // Construct a photo object
                        InstagramPhoto photo = new InstagramPhoto();
                        if (userName != null) {
                            photo.setUserName(userName);
                        }
                        else {
                            photo.setUserName("");
                        }
                        if (profilePicUrl != null) {
                            photo.setProfilePicUrl(profilePicUrl);
                        }
                        else {
                            photo.setProfilePicUrl("");
                        }
                        if (caption != null) {
                            photo.setCaption(caption);
                        }
                        else {
                            photo.setCaption("");
                        }
                        if (location != null) {
                            photo.setLocation(location);
                        }
                        else {
                            photo.setLocation("");
                        }
                        if (imageUrl != null) {
                            photo.setImageUrl(imageUrl);
                        }
                        else {
                            photo.setImageUrl("");
                        }
                        if (creationTimeStr != null) {
                            photo.setCreationTimeStr(creationTimeStr);
                        }
                        else {
                            photo.setCreationTimeStr("");
                        }
                        photo.setCreationTime(creationTime);
                        photo.setImageWidth(imageWidth);
                        photo.setImageHeight(imageHeight);
                        photo.setLikesCount(likesCount);

                        // Add object to the array
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Notify that the data has changed and refresh the adapter
                adapterPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    // Convert creation time of the images relative to current time
    private String findRelativeCreationTime(long creationTime) {
        String creationTimeStrTemp = null;
        String creationTimeStr = null;
        creationTimeStrTemp = DateUtils.getRelativeTimeSpanString(creationTime*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        creationTimeStr = creationTimeStrTemp.replaceAll("ago", "");
        creationTimeStr = creationTimeStr.replaceAll("seconds", "s");
        creationTimeStr = creationTimeStr.replaceAll("second", "s");
        creationTimeStr = creationTimeStr.replaceAll("secs", "s");
        creationTimeStr = creationTimeStr.replaceAll("sec", "s");
        creationTimeStr = creationTimeStr.replaceAll("minutes", "m");
        creationTimeStr = creationTimeStr.replaceAll("minute", "m");
        creationTimeStr = creationTimeStr.replaceAll("mins", "m");
        creationTimeStr = creationTimeStr.replaceAll("min", "m");
        creationTimeStr = creationTimeStr.replaceAll("hours", "h");
        creationTimeStr = creationTimeStr.replaceAll("hour", "h");
        creationTimeStr = creationTimeStr.replaceAll("hrs", "h");
        creationTimeStr = creationTimeStr.replaceAll("hr", "h");
        creationTimeStr = creationTimeStr.replaceAll("\\s", "");
        return creationTimeStr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
