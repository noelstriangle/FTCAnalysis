package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.drm.DrmStore;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.LinearLayout;
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
    public Intent intent;

    private TableLayout tbl;

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

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();

        navigation = findViewById(R.id.navigation);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Scouting");

        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

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

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.scoutTeam:

                        new Toast(MainActivity.this).makeText(MainActivity.this, "Already in Scouting", Toast.LENGTH_LONG).show();

                        break;

                    case R.id.matchAnalysis:

                        Intent fadjk = new Intent(MainActivity.this, MatchAnalyseActivity.class);
                        startActivity(fadjk);


                    break;

                }

                return true;
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
        MenuItem toHidef = menu.findItem(R.id.action_edit);
        toHidef.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        switch(item.getItemId()){

            case R.id.action_settings:

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

                deleteBuilder.setTitle("Delete Database");

                deleteBuilder.setMessage("Deleteing all the teams will delete all the teams. Maybe export before you do this? Understand?");

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

    public void initStuff() {

        tbl = findViewById(R.id.mainTable);
        tbl.removeAllViewsInLayout();
        TableRow tblrow0 = new TableRow(this);

        TextView txt0 = new TextView(this);
        txt0.setText("  Team Number  ");
        txt0.setTextColor(android.graphics.Color.rgb(33,81,8));
        tblrow0.addView(txt0);

        TextView txt1 = new TextView(this);
        txt1.setText("  Team Name  ");
        txt1.setTextColor(android.graphics.Color.rgb(33,81,8));
        tblrow0.addView(txt1);

        TextView txt2 = new TextView(this);
        txt2.setText("  Total Points  ");
        txt2.setTextColor(android.graphics.Color.rgb(33,81,8));
        tblrow0.addView(txt2);

        TextView txt3 = new TextView(this);
        txt3.setText("  Details  ");
        txt3.setTextColor(android.graphics.Color.rgb(33,81,8));
        tblrow0.addView(txt3);

        tbl.addView(tblrow0);

        Log.d("INITIALIZATION/RESUMING", "COLUMN HEADERS ADDED.");

        Log.d("INITIALIZATION/RESUMING", "STARTING FOR LOOP");
        for(int i = 0; i < numberOfTeams; i++) {

            final int tN = teamIds[i];

            TableRow tblrow = new TableRow(this);

            Log.d("INITIALIZATION/RESUMING", "TableRow: " + String.valueOf(i));

            teamTotal = teamAutos[i] + teamTeles[i];

            Log.d("INITIALIZATION/RESUMING", "teamTotal: " + String.valueOf(i) + ": " + String.valueOf(teamTotal));

            TextView tv1 = new TextView(this);
            tv1.setText(String.valueOf(teamNums[i]));
            tv1.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv1.setGravity(Gravity.CENTER);
            tblrow.addView(tv1);
            Log.d("INITIALIZATION/RESUMING", "TEXT 1(" + String.valueOf(teamNums[i]) + ") added.");
            TextView tv2 = new TextView(this);
            tv2.setText(String.valueOf(shortenText(teamNames[i])));
            tv2.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv2.setGravity(Gravity.CENTER);
            tblrow.addView(tv2);
            Log.d("INITIALIZATION/RESUMING", "TEXT 2(" + String.valueOf(teamNames[i]) + ") added.");
            TextView tv3 = new TextView(this);
            tv3.setText(String.valueOf(teamTotal));
            tv3.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv3.setGravity(Gravity.CENTER);
            tblrow.addView(tv3);
            Log.d("INITIALIZATION/RESUMING", "TEXT 3(" + String.valueOf(teamTotal) + ") added.");
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.mipmap.no_image);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startTeamDetails(view, tN);

                }
            });
            tblrow.addView(button);
            tbl.addView(tblrow);
            Log.d("INITIALIZATION/RESUMING", "TableRow added to Tabel.");

        }

    }
    public String shortenText(String s) {

        if (s.length() > 12) {

            s = StringUtils.abbreviate(s, 15);
            return s;

        } else {

            return s;

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        tbl = findViewById(R.id.mainTable);
        tbl.removeAllViewsInLayout();

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
                    teamNames[i] = tempTeam.getTeamName();
                    teamAutos[i] = tempTeam.getAutoPoints();
                    teamTeles[i] = tempTeam.getTelePoints();

                    Log.d("RESUMING", "teamInfo Updated: " + teamNums[i] + " " + teamNames[i] + " " + teamAutos[i] + " " + teamTeles[i]);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initStuff();
                    }
                });

            }
        }).start();

        for(int i = 0; i < numberOfTeams; i++) {

            TableRow tblrow = new TableRow(this);

            teamTotal = teamAutos[i] + teamTeles[i];

            TextView tv1 = new TextView(this);
            tv1.setText(String.valueOf(teamNums[i]));
            tv1.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv1.setGravity(Gravity.CENTER);
            tblrow.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setText(String.valueOf(teamNames[i]));
            tv2.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv2.setGravity(Gravity.CENTER);
            tblrow.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setText(String.valueOf(teamTotal));
            tv3.setTextColor(android.graphics.Color.rgb(33,81,8));
            tv3.setGravity(Gravity.CENTER);
            tblrow.addView(tv3);

            tblrow.setMinimumHeight(20);
            tbl.addView(tblrow);

        }

    }

    public void startTeamDetails(View view, int teamNum) {

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
