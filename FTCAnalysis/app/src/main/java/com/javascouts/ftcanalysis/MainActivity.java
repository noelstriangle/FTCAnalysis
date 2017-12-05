package com.javascouts.ftcanalysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(android.graphics.Color.rgb(255,255,255));

        //myToolbar.setLogo();

    }

    public void scoutNewTeam(View view) {

        Intent intent = new Intent(this, ScoutTeamActivity.class);
        startActivity(intent);

    }

}
