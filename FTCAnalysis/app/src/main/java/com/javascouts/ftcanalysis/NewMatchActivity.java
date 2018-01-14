package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Room;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class NewMatchActivity extends AppCompatActivity {

    TeamDatabase mDb;
    TeamDao mDao;
    List<Team> teams;
    int numberOfTeams;
    Integer[] teamNums;
    Team tempTeam;
    Spinner bs1, bs2, rs1, rs2;
    TextView matchText;
    int b1, b2, r1, r2, mN;
    Match tempMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        getInfoAndStartUi();

    }

    private void getInfoAndStartUi() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();

                Log.d("RESUMING", "Database Instantiated.");

                teams = new ArrayList<>();

                teams = mDao.getAllAndSort();
                numberOfTeams = teams.size();

                Log.d("RESUMING", "numberOfTeams: " + String.valueOf(numberOfTeams));

                teamNums = new Integer[teams.size()];

                Log.d("RESUMING", "Arrays Created.");

                for(int i = 0; i < numberOfTeams; i++) {

                    tempTeam = teams.get(i);

                    teamNums[i] = (Integer) tempTeam.getTeamNumber();

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    startUi(teamNums);

                    }
                });

            }
        }).start();
    }

    private void startUi(Integer[] teamNumbers) {

        bs1 = findViewById(R.id.blue1spinner);
        bs2 = findViewById(R.id.blue2spinner);
        rs1 = findViewById(R.id.red1spinner);
        rs2 = findViewById(R.id.red2spinner);
        matchText = findViewById(R.id.ABCDE);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teamNums);

        bs1.setAdapter(adapter);
        bs2.setAdapter(adapter);
        rs1.setAdapter(adapter);
        rs2.setAdapter(adapter);

    }

    public void setInfoAndKill(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                tempMatch = new Match();

                b1 = Integer.valueOf(bs1.getSelectedItem().toString());
                b2 = Integer.valueOf(bs2.getSelectedItem().toString());
                r1 = Integer.valueOf(rs1.getSelectedItem().toString());
                r2 = Integer.valueOf(rs2.getSelectedItem().toString());

                try {

                    mN = Integer.valueOf(matchText.getText().toString());
                    tempMatch.setMatchNumber(mN);
                    tempMatch.setBlue1(b1);
                    tempMatch.setBlue2(b2);
                    tempMatch.setRed1(r1);
                    tempMatch.setRed2(r2);
                    tempMatch.setBlue1id(mDao.getIdByTeamNumber(b1));
                    tempMatch.setBlue2id(mDao.getIdByTeamNumber(b2));
                    tempMatch.setRed1id(mDao.getIdByTeamNumber(r1));
                    tempMatch.setRed2id(mDao.getIdByTeamNumber(r2));

                } catch(NumberFormatException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = new Toast(NewMatchActivity.this).makeText(NewMatchActivity.this, "One or more fields are not filled out. Or something.", Toast.LENGTH_SHORT);
                            t.show();
                            }
                    });

                    return;

                }

                mDao.insertMatch(tempMatch);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        finish();

                    }
                });

            }

        }).start();


    }

}
