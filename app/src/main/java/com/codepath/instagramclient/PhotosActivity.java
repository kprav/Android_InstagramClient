package com.codepath.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

                        // Construct a photo object
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUserName(photoJSON.getJSONObject("user").getString("username"));
                        photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));

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
