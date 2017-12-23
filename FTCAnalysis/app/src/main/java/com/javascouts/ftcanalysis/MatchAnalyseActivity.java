package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Liam on 12/21/2017.
 */

public class MatchAnalyseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analyse);

        toolbar = findViewById(R.id.toolbarMA);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Match Analysis");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        MenuItem toHide = menu.findItem(R.id.action_delete);
        toHide.setVisible(false);
        MenuItem toHidef = menu.findItem(R.id.action_edit);
        toHidef.setVisible(false);
        MenuItem toHideg = menu.findItem(R.id.action_deleteall);
        toHideg.setVisible(false);
        MenuItem toHidei = menu.findItem(R.id.action_export);
        toHidei.setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {

                case R.id.scoutTeam:

                    new Toast(this).setText("Already in Scouting");

                    break;

                case R.id.matchAnalysis:

                    new Intent(this, MatchAnalyseActivity.class);

                    break;

            }
            return true;
        }

        switch(item.getItemId()){

            case R.id.action_settings:

                break;

        }

        return true;

    }

}
