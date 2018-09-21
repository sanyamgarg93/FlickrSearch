package com.uber.flickrsearch.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uber.flickrsearch.Fragments.ImageListFragment;
import com.uber.flickrsearch.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplication(), ImageSearchActivity.class);
        startActivity(intent);

        finish();
    }
}
