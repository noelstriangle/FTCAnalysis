package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liam on 12/21/2017.
 */

public class MatchAnalyseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerList;
    private ListView listView;
    private NavigationView navigation;
    private TeamDatabase mDb;
    private TeamDao mDao;
    private List<Match> matches;
    private static Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analyse);

        toolbar = findViewById(R.id.toolbarMA);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        getSupportActionBar().setTitle("Match Analysis");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigation = findViewById(R.id.navigation);

        t = Toast.makeText(this, "Already In Analysis", Toast.LENGTH_SHORT);

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

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.scoutTeam:

                        startActivity(new Intent(MatchAnalyseActivity.this, MainActivity.class));
                        mDrawerLayout.closeDrawers();

                        break;

                    case R.id.matchAnalysis:

                        t.show();
                        mDrawerLayout.closeDrawers();

                        break;

                }

                return true;
            }
        });

        init();

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
        MenuItem toHidei = menu.findItem(R.id.action_export);
        toHidei.setVisible(false);
        toHide = menu.findItem(R.id.action_clearPrefs);
        toHide.setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        switch(item.getItemId()){

            case R.id.action_settings:

                break;

            case R.id.action_deleteall:

                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);

                deleteBuilder.setTitle("Delete Matches");

                deleteBuilder.setMessage("Deleting all the teams will delete all the teams.");

                deleteBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                for(int i = matches.size()-1; i >= 0; i--) {

                                    mDao.deleteMatch(mDao.getMatchByMatchNumber(matches.get(i).getMatchNumber()));

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recreate();
                                    }
                                });

                            }
                        }).start();

                    }
                });
                deleteBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog deleteDialog = deleteBuilder.create();

                deleteDialog.show();

                break;

        }

        return true;

    }

    public void addNewMatch(View view) {

        Intent newIntent = new Intent(this, NewMatchActivity.class);
        startActivity(newIntent);

    }

    public void init() {

        final SwipeRefreshLayout srl = findViewById(R.id.swiperefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mDb = Room.databaseBuilder(getApplicationContext(),
                                TeamDatabase.class, "team-database").build();
                        mDao = mDb.getTeamDao();

                        Log.d("RESUMING", "Database Instantiated.");

                        matches = new ArrayList<>();

                        matches = mDao.getMatchesAndSort();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                listView = findViewById(R.id.listView);
                                listView.removeAllViewsInLayout();
                                MatchAdapter matchAdapter = new MatchAdapter(MatchAnalyseActivity.this, R.layout.content_match_row, matches);
                                listView.setAdapter(matchAdapter);
                                srl.setRefreshing(false);

                            }
                        });

                    }
                }).start();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();

                Log.d("RESUMING", "Database Instantiated.");

                matches = new ArrayList<>();

                matches = mDao.getMatchesAndSort();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        listView = findViewById(R.id.listView);
                        listView.removeAllViewsInLayout();
                        MatchAdapter matchAdapter = new MatchAdapter(MatchAnalyseActivity.this, R.layout.content_match_row, matches);
                        listView.setAdapter(matchAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                TextView tv = view.findViewById(R.id.mN);
                                startMatchDetails(Integer.valueOf(tv.getText().toString()));

                            }
                        });

                    }
                });

            }
        }).start();

    }

    public void startMatchDetails(int matchNum) {

        Intent intent = new Intent(this, MatchDetailsActivity.class);
        intent.putExtra("MATCH_NUMBER", matchNum);
        //Log.d("TESTING", "Extra:" + String.valueOf(matchNum) + " set.");
        startActivity(intent);

    }

}
