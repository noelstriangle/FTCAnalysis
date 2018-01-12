package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.drm.DrmStore;
import android.media.Image;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public int numberOfTeams;

    private TeamDatabase mDb;
    private TeamDao mDao;
    
    public int[] teamNums;
    public String[] teamNames;
    public int[] teamAutos;
    public int[] teamTeles, teamIds;
    public int teamTotal;
    private ListView mListView;
    public Intent intent;

    private ListView tbl;
    private static Toast t;
    public List<Team> teams;
    public Team tempTeam;
    public List<Team> resumeTeams;
    public Team[] deleteTeams;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private String[] mDrawerList;
    private ListView mDrawerListView;
    private CharSequence mTitle;
    private NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();

            }
        }).start();

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(MainActivity.this, FirstRunActivity.class));

        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);

        navigation = findViewById(R.id.navigation);

        ActionBar actionBar = getSupportActionBar();

        t = Toast.makeText(this, "Already In Scouting", Toast.LENGTH_SHORT);

        actionBar.setTitle("Scouting");

        myToolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

                        resumeTeams = new ArrayList<>();
                        teams = new ArrayList<>();

                        teams = mDao.getAllAndSort();
                        numberOfTeams = teams.size();

                        Log.d("RESUMING", "numberOfTeams: " + String.valueOf(numberOfTeams));

                        teamNums = new int[teams.size()];
                        teamNames = new String[teams.size()];
                        teamAutos = new int[teams.size()];
                        teamTeles = new int[teams.size()];
                        teamIds = new int[teams.size()];

                        Log.d("RESUMING", "Arrays Created.");

                        for(int i = 0; i < numberOfTeams; i++) {

                            tempTeam = teams.get(i);

                            teamIds[i] = tempTeam.getId();
                            teamNums[i] = tempTeam.getTeamNumber();
                            teamNames[i] = shortenText(tempTeam.getTeamName());
                            teamAutos[i] = tempTeam.getAutoPoints();
                            teamTeles[i] = tempTeam.getTelePoints();

                            Log.d("RESUMING", "teamInfo Updated: " + teamNums[i] + " " + teamNames[i] + " " + teamAutos[i] + " " + teamTeles[i]);

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initStuff();
                                srl.setRefreshing(false);

                            }
                        });

                    }
                }).start();

            }
        });

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.scoutTeam:

                        t.show();
                        mDrawerLayout.closeDrawers();

                        break;

                    case R.id.matchAnalysis:

                        Intent fadjk = new Intent(MainActivity.this, MatchAnalyseActivity.class);
                        mDrawerLayout.closeDrawers();
                        startActivity(fadjk);

                    break;

                    case R.id.action_about:

                        Intent yah = new Intent(MainActivity.this, AboutActivity.class);
                        mDrawerLayout.closeDrawers();
                        startActivity(yah);

                        break;

                    case R.id.action_reset:

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setTitle("Reset Everything");

                        builder.setMessage("If you reset everything, your teams will be reset, your matches will be reset, and your preferences will be reset. Are you sure you want to proceed?");

                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sp = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                                sp.edit().clear().apply();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        for(int i = numberOfTeams-1; i >= 0; i--) {

                                            mDao.deleteAll(mDao.getTeam(teamIds[i]));

                                        }

                                    }
                                }).start();

                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        AlertDialog dialog = builder.create();

                        dialog.show();

                        break;

                }

                return false;
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        MenuItem toHide = menu.findItem(R.id.action_delete);
        toHide.setVisible(false);
        toHide = menu.findItem(R.id.action_edit);
        toHide.setVisible(false);
        toHide = menu.findItem(R.id.action_settings);
        toHide.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        switch(item.getItemId()){

            case R.id.action_settings:

                startSettings();

                break;

            case R.id.action_export:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Export Database");

                builder.setMessage("Exporting this database will create a .csv file containing the data you have scouted. The file will be located in your Downloads folder. Proceed?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                exportToDatabase();

                            }
                        }).start();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                break;

            case R.id.action_deleteall:

                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);

                deleteBuilder.setTitle("Delete Teams");

                deleteBuilder.setMessage("Deleting all the teams will delete all the teams. Maybe export before you do this? Understand?");

                deleteBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                for(int i = numberOfTeams-1; i >= 0; i--) {

                                    mDao.deleteAll(mDao.getTeam(teamIds[i]));

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

    public void scoutNewTeam(View view) {

        Intent intent = new Intent(this, ScoutTeamActivity.class);
        startActivity(intent);

    }

    public void startSettings() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    public void initStuff() {

        mListView = findViewById(R.id.listView);
        mListView.removeAllViewsInLayout();
        TeamAdapter teamAdapter = new TeamAdapter(this, R.layout.content_row, teams);
        mListView.setAdapter(teamAdapter);
        //mListView.setEmptyView(findViewById(R.id.empty));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = view.findViewById(R.id.subTitleText);
                startTeamDetails(Integer.valueOf(tv.getText().toString()));

            }
        });


    }
    public String shortenText(String s) {

        if (s.length() > 15) {

            s = StringUtils.abbreviate(s, 18);
            return s;

        } else {

            return s;

        }

    }

    @Override
    protected void onResume() {

        mListView = findViewById(R.id.listView);
        mListView.removeAllViewsInLayout();

        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();

                Log.d("RESUMING", "Database Instantiated.");

                resumeTeams = new ArrayList<>();
                teams = new ArrayList<>();

                teams = mDao.getAllAndSort();
                numberOfTeams = teams.size();

                Log.d("RESUMING", "numberOfTeams: " + String.valueOf(numberOfTeams));

                teamNums = new int[teams.size()];
                teamNames = new String[teams.size()];
                teamAutos = new int[teams.size()];
                teamTeles = new int[teams.size()];
                teamIds = new int[teams.size()];

                Log.d("RESUMING", "Arrays Created.");

                for(int i = 0; i < numberOfTeams; i++) {

                    tempTeam = teams.get(i);

                    teamIds[i] = tempTeam.getId();
                    teamNums[i] = tempTeam.getTeamNumber();
                    teamNames[i] = shortenText(tempTeam.getTeamName());
                    teamAutos[i] = tempTeam.getAutoPoints();
                    teamTeles[i] = tempTeam.getTelePoints();

                    Log.d("RESUMING", "teamInfo Updated: " + teamIds[i] + " " + teamNums[i] + " " + teamNames[i] + " " + teamAutos[i] + " " + teamTeles[i]);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initStuff();
                    }
                });

            }
        }).start();

    }

    public void startTeamDetails(int teamNum) {

        Intent intent = new Intent(this, TeamDetailsActivity.class);
        intent.putExtra("TEAM_NUMBER", teamNum);
        Log.d("TESTING", "Extra:" + String.valueOf(teamNum) + " set.");
        startActivity(intent);

    }

    @Override
    protected void onPause() {

        super.onPause();

        mDb.close();

    }

    public boolean exportToDatabase() {

        String FILENAME = "teamDatabase.csv";
        File directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File logDir = new File(directoryDownload, FILENAME);
        try {
            logDir.createNewFile();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(logDir));
            Cursor curCSV = mDb.query("SELECT * FROM team", null);
            csvWriter.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = { curCSV.getString(1)+ " ", curCSV.getString(2)+ " ",
                        curCSV.getString(3)+ " ", curCSV.getString(4)+ " ",
                        curCSV.getString(5)+ " ", curCSV.getString(6)+ " ",
                        curCSV.getString(7)+ " ", curCSV.getString(8)+ " ",
                        curCSV.getString(9)+ " ", curCSV.getString(10)+ " ",
                        curCSV.getString(10)+ " ", curCSV.getString(11)+ " ",
                        curCSV.getString(12)+ " ", curCSV.getString(13)+ " ",
                        curCSV.getString(14)+ " "};
                csvWriter.writeNext(arrStr);
            }
            csvWriter.close();
            curCSV.close();
            return true;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return false;
        }


    }

}
