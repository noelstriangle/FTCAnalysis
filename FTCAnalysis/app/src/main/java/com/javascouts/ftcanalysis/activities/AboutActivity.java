package com.javascouts.ftcanalysis.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.javascouts.ftcanalysis.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("About");

        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
