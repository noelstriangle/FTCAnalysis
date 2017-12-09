package com.javascouts.ftcanalysis;

import android.app.ActionBar;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Liam on 12/9/2017.
 */

public class TeamDetailsActivity extends AppCompatActivity {

    private TeamDatabase mDb;
    private TeamDao mDao;

    private LinearLayout llayout;

    private int teamN;

    private Team tempTeam;
    private String teamName;
    private boolean canJewel, canGlyphAuto, canCypher, canSafeZone, canCypherEndgame, canUpright;
    private int teamNumber, glyphs, rows, columns, relics, relicZone, autoPoints, telePoints;

    @Override
    protected void onCreate(Bundle savedInstnceState) {

        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_team_details);

        llayout = findViewById(R.id.llayout);

        teamN = getIntent().getIntExtra("TEAM_NUMBER", 0);
        Log.d("TESTING", "Extra:" + String.valueOf(teamN) + " received.");

        init(teamN);

    }

    public void init(final int teamNum) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();
                Log.d("DETAILCREATION","Database instantiated.");

                tempTeam = mDao.getTeam(teamNum);
                Log.d("DETAILCREATIION", "Team:" + String.valueOf(teamNum) + " received.");

                teamName = tempTeam.getTeamName();
                teamNumber = tempTeam.getTeamNumber();
                Log.d("DETAILCREATION", "Name: " + teamName + ". Number: " + String.valueOf(teamNumber) + ".");
                canJewel = tempTeam.getJewelb();
                canCypher = tempTeam.getAutoCypherb();
                canGlyphAuto = tempTeam.getGlyphAutob();
                canSafeZone = tempTeam.getSafeZoneb();
                canCypherEndgame = tempTeam.getEndGameCypherb();
                canUpright = tempTeam.getUprightb();
                glyphs = tempTeam.getGlyphBari();
                rows = tempTeam.getRowBari();
                columns = tempTeam.getColumnBari();
                relics = tempTeam.getRelicBari();
                relicZone = tempTeam.getRelicZoneBari();
                autoPoints = tempTeam.getAutoPoints();
                telePoints = tempTeam.getTelePoints();
                Log.d("DETAILCREATION", "Team info all received.");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initUi();
                    }
                });
            }
        }).start();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(android.graphics.Color.rgb(0,155,25));

    }

    public void initUi() {

        TextView teamNameText = new TextView(this);
        teamNameText.setText(teamName);
        teamNameText.setTextSize(32);
        teamNameText.setTop(0);
        teamNameText.setTextColor(android.graphics.Color.rgb(0,155,25));
        teamNameText.setGravity(Gravity.CENTER);
        TextView teamNumText = new TextView(this);
        teamNumText.setText(String.valueOf(teamNumber));
        teamNumText.setTextSize(24);
        teamNumText.setTop(36);
        teamNumText.setTextColor(android.graphics.Color.rgb(0,155,25));
        teamNumText.setGravity(Gravity.CENTER);

        llayout.addView(teamNameText);
        llayout.addView(teamNumText);

    }
}
