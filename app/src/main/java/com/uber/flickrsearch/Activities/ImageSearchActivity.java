package com.uber.flickrsearch.Activities;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uber.flickrsearch.Fragments.ImageListFragment;
import com.uber.flickrsearch.Network.HTTPApiCallService;
import com.uber.flickrsearch.Network.HTTPApiCallInterface;
import com.uber.flickrsearch.Models.FlickrImageModel;
import com.uber.flickrsearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageSearchActivity extends AppCompatActivity implements ImageListFragment.ImageListFragmentInterface{

    private int apiPage = 1;
    private ImageListFragment imageListFragment;
    private String searchString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        imageListFragment = new ImageListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_image_search_frame_layout, imageListFragment)
                .commit();

        final EditText searchEditText = findViewById(R.id.activity_image_search_edit_text);
        ImageButton searchImageButton = findViewById(R.id.activity_image_search_image_button);

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = searchEditText.getText().toString();
                if (!searchString.isEmpty()) {
                    imageListFragment.clearImages();

                    apiPage = 1;
                    fetchFlickrImages();
                }
                hideKeyboard();
            }
        });
    }

    void fetchFlickrImages() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                "&api_key=3e7cc266ae2b0e0d78e279ce8e361736" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&safe_search=1" +
                "&text=" + searchString +
                "&page=" + apiPage +
                "&per_page=20";

        HTTPApiCallInterface mResultCallback = new HTTPApiCallInterface() {
            @Override
            public void notifySuccess(JSONObject response) {
                parseAPIResponse(response);
            }

            @Override
            public void notifyError(JSONObject error) {
            }
        };

        HTTPApiCallService httpApiCallService = new HTTPApiCallService(mResultCallback);
        httpApiCallService.getHTTPApiData(url);
    }

    void parseAPIResponse(JSONObject response) {
        try {
            if (!response.getString("stat").equals("ok"))
                return;

            JSONObject photos = response.getJSONObject("photos");
            int page = photos.getInt("page");
            int pages = photos.getInt("pages");
            JSONArray photoList = photos.getJSONArray("photo");

            ArrayList<FlickrImageModel> flickrImageModels = new ArrayList<>();
            for (int i=0; i<photoList.length(); i++) {
                flickrImageModels.add(new FlickrImageModel(photoList.getJSONObject(i)));
            }
            imageListFragment.populateImages(flickrImageModels);

            apiPage = page + 1;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGridViewScrollEnd() {
        fetchFlickrImages();
    }

    void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null && this.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
