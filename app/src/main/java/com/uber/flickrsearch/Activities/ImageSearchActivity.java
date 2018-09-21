package com.uber.flickrsearch.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uber.flickrsearch.Fragments.ImageListFragment;
import com.uber.flickrsearch.R;

public class ImageSearchActivity extends AppCompatActivity implements ImageListFragment.ImageListFragmentInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        Fragment imageListFragment = new ImageListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_image_search_frame_layout, imageListFragment)
                .commit();
    }

    @Override
    public void onGridViewScrollEnd() {

    }
}
