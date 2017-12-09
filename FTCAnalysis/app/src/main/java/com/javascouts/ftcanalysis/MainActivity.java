package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public int numberOfTeams;

    private TeamDatabase mDb;
    private TeamDao mDao;
    
    public int[] teamNums;
    public String[] teamNames;
    public int[] teamAutos;
    public int[] teamTeles;
    public int teamTotal;

    private TableLayout tbl;

    public List<Team> teams;
    public Team tempTeam;
    public List<Team> resumeTeams;

    private boolean FIRST_RUN = true;

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

                        Log.d("CREATION", "Database Instantiated.");

                        resumeTeams = new ArrayList<>();
                        teams = new ArrayList<>();

                        teams = mDao.getAll();
                        numberOfTeams = teams.size();

                        Log.d("CREATION", "numberOfTeams: " + String.valueOf(numberOfTeams));

                        teamNums = new int[teams.size()];
                        teamNames = new String[teams.size()];
                        teamAutos = new int[teams.size()];
                        teamTeles = new int[teams.size()];

                        Log.d("CREATION", "Arrays Created.");

                        for(int i = 0; i < numberOfTeams; i++) {

                            tempTeam = teams.get(i);

                            teamNums[i] = tempTeam.getTeamNumber();
                            teamNames[i] = tempTeam.getTeamName();
                            teamAutos[i] = tempTeam.getAutoPoints();
                            teamTeles[i] = tempTeam.getTelePoints();

                            Log.d("CREATION", "teamInfo Updated: " + teamNums[i] + " " + teamNames[i] + " " + teamAutos[i] + " " + teamTeles[i]);

                        }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initStuff();
                    }
                });

            }
        }).start();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(android.graphics.Color.rgb(0,155,25));

        //myToolbar.setLogo();

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
        txt0.setTextColor(android.graphics.Color.rgb(0,155,25));
        tblrow0.addView(txt0);

        TextView txt1 = new TextView(this);
        txt1.setText("  Team Name  ");
        txt1.setTextColor(android.graphics.Color.rgb(0,155,25));
        tblrow0.addView(txt1);

        TextView txt2 = new TextView(this);
        txt2.setText("  Total Points  ");
        txt2.setTextColor(android.graphics.Color.rgb(0,155,25));
        tblrow0.addView(txt2);

        TextView txt3 = new TextView(this);
        txt3.setText("  Details  ");
        txt3.setTextColor(android.graphics.Color.rgb(0,155,25));
        tblrow0.addView(txt3);

        tbl.addView(tblrow0);

        Log.d("INITIALIZATION/RESUMING", "COLUMN HEADERS ADDED.");

        Log.d("INITIALIZATION/RESUMING", "STARTING FOR LOOP");
        for(int i = 0; i < numberOfTeams; i++) {

            final int tN = teamNums[i];

            TableRow tblrow = new TableRow(this);

            Log.d("INITIALIZATION/RESUMING", "TableRow: " + String.valueOf(i));

            teamTotal = teamAutos[i] + teamTeles[i];

            Log.d("INITIALIZATION/RESUMING", "teamTotal: " + String.valueOf(i) + ": " + String.valueOf(teamTotal));

            TextView tv1 = new TextView(this);
            tv1.setText(String.valueOf(teamNums[i]));
            tv1.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv1.setGravity(Gravity.CENTER);
            tblrow.addView(tv1);
            Log.d("INITIALIZATION/RESUMING", "TEXT 1(" + String.valueOf(teamNums[i]) + ") added.");
            TextView tv2 = new TextView(this);
            tv2.setText(String.valueOf(teamNames[i]));
            tv2.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv2.setGravity(Gravity.CENTER);
            tblrow.addView(tv2);
            Log.d("INITIALIZATION/RESUMING", "TEXT 2(" + String.valueOf(teamNames[i]) + ") added.");
            TextView tv3 = new TextView(this);
            tv3.setText(String.valueOf(teamTotal));
            tv3.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv3.setGravity(Gravity.CENTER);
            tblrow.addView(tv3);
            Log.d("INITIALIZATION/RESUMING", "TEXT 3(" + String.valueOf(teamTotal) + ") added.");
            Button button = new Button(this);
            button.setText("");
            button.setWidth(10);
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

                teams = mDao.getAll();
                numberOfTeams = teams.size();

                Log.d("RESUMING", "numberOfTeams: " + String.valueOf(numberOfTeams));

                teamNums = new int[teams.size()];
                teamNames = new String[teams.size()];
                teamAutos = new int[teams.size()];
                teamTeles = new int[teams.size()];

                Log.d("RESUMING", "Arrays Created.");

                for(int i = 0; i < numberOfTeams; i++) {

                    tempTeam = teams.get(i);

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

            Log.d("RESUMING", "TableRow: " + String.valueOf(i));

            teamTotal = teamAutos[i] + teamTeles[i];

            Log.d("RESUMING", "teamTotal: " + String.valueOf(i) + ": " + String.valueOf(teamTotal));

            TextView tv1 = new TextView(this);
            tv1.setText(String.valueOf(teamNums[i]));
            tv1.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv1.setGravity(Gravity.CENTER);
            tblrow.addView(tv1);
            Log.d("RESUMING", "TEXT 1(" + String.valueOf(teamNums[i]) + ") added.");
            TextView tv2 = new TextView(this);
            tv2.setText(String.valueOf(teamNames[i]));
            tv2.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv2.setGravity(Gravity.CENTER);
            tblrow.addView(tv2);
            Log.d("RESUMING", "TEXT 2(" + String.valueOf(teamNames[i]) + ") added.");
            TextView tv3 = new TextView(this);
            tv3.setText(String.valueOf(teamTotal));
            tv3.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv3.setGravity(Gravity.CENTER);
            tblrow.addView(tv3);
            Log.d("RESUMING", "TEXT 3(" + String.valueOf(teamTotal) + ") added.");
            tblrow.setMinimumHeight(20);
            tbl.addView(tblrow);
            Log.d("RESUMING", "TableRow added to Tabel.");

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

}
