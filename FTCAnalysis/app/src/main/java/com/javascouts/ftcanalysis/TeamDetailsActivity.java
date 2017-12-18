package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

/**
 * Created by Liam on 12/9/2017.
 */

public class TeamDetailsActivity extends AppCompatActivity {

    private TeamDatabase mDb;
    private TeamDao mDao;

    private LinearLayout llayout;

    private int teamN;

    private Toast toast1;

    private Team toDelete;
    private Team tempTeam;
    private String teamName, teamInfo;
    private boolean canJewel, canGlyphAuto, canCypher, canSafeZone, canCypherEndgame, canUpright;
    private int teamNumber, glyphs, rows, columns, relics, relicZone, autoPoints, telePoints;

    @Override
    protected void onCreate(Bundle savedInstnceState) {

        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_team_details);

    }

    @Override
    protected void onResume() {

        super.onResume();
        llayout = findViewById(R.id.llayout);

        teamN = getIntent().getIntExtra("TEAM_NUMBER", 0);
        Log.d("TESTING", "Extra:" + String.valueOf(teamN) + " received.");

        init(teamN);

        toast1 = new Toast(this).makeText(this, "Delete Canceled.", Toast.LENGTH_LONG);
        toast1.setText("Delete Canceled.");

        Toolbar myToolbar = findViewById(R.id.toolbarTD);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Team Details");

        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        MenuItem toHide1 = menu.findItem(R.id.action_export);
        toHide1.setVisible(false);
        MenuItem toHide2 = menu.findItem(R.id.action_deleteall);
        toHide2.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_settings:

                break;

            case R.id.action_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Delete team " + String.valueOf(teamNumber) + " forever?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                toDelete = mDao.getTeam(teamN);
                                mDao.deleteAll(toDelete);
                            }
                        }).start();

                        finish();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        toast1.show();

                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);

                break;

            case R.id.action_edit:

                editTeamDetails(teamN);

                break;


        }

        return true;

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
                teamInfo = tempTeam.getOtherNotes();

                Log.d("DETAILCREATION", "Team info all received.");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initUi();
                    }
                });
            }
        }).start();

        Toolbar myToolbar = findViewById(R.id.toolbarTD);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initUi() {

        int x = 16;

        TextView teamNameText = new TextView(this);
        teamNameText.setText(teamName);
        teamNameText.setTextSize(32);
        teamNameText.setTop(0 * x);
        teamNameText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNameText.setGravity(Gravity.CENTER);

        TextView teamNumText = new TextView(this);
        teamNumText.setText(String.valueOf(teamNumber));
        teamNumText.setTextSize(24);
        teamNumText.setTop(1 * x);
        teamNumText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNumText.setGravity(Gravity.CENTER);

        TextView autonomous = new TextView(this);
        autonomous.setText("Autonomous:");
        autonomous.setTextSize(16);
        autonomous.setTop(2 * x);
        autonomous.setTextColor(android.graphics.Color.rgb(33,81,8));
        autonomous.setGravity(Gravity.START);

        TextView canJewelText = new TextView(this);
        canJewelText.setText("- " + returnCanOrCant(canJewel) + " knock the correct jewel off.");
        canJewelText.setTextSize(16);
        canJewelText.setTop(3 * x);
        canJewelText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canJewelText.setGravity(Gravity.START);

        TextView canGlyphAutoText = new TextView(this);
        canGlyphAutoText.setText("- " + returnCanOrCant(canGlyphAuto) + " place the glyph in the box.");
        canGlyphAutoText.setTextSize(16);
        canGlyphAutoText.setTop(4 * x);
        canGlyphAutoText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canGlyphAutoText.setGravity(Gravity.START);

        TextView canAutoCypherText = new TextView(this);
        canAutoCypherText.setText("- " + returnCanOrCant(canCypher) + " place the glyph in the correct column.");
        canAutoCypherText.setTextSize(16);
        canAutoCypherText.setTop(5 * x);
        canAutoCypherText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canAutoCypherText.setGravity(Gravity.START);

        TextView canSafeZoneText = new TextView(this);
        canSafeZoneText.setText("- " + returnCanOrCant(canSafeZone) + " park in the safe zone.");
        canSafeZoneText.setTextSize(16);
        canSafeZoneText.setTop(6 * x);
        canSafeZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canSafeZoneText.setGravity(Gravity.START);

        TextView teleop = new TextView(this);
        teleop.setText("Teleop:");
        teleop.setTextSize(16);
        teleop.setTop(7 * x);
        teleop.setTextColor(android.graphics.Color.rgb(33,81,8));
        teleop.setGravity(Gravity.START);

        TextView glyphText = new TextView(this);
        glyphText.setText("- Can score " + String.valueOf(glyphs) + " glyphs.");
        glyphText.setTextSize(16);
        glyphText.setTop(8 * x);
        glyphText.setTextColor(android.graphics.Color.rgb(33,81,8));
        glyphText.setGravity(Gravity.START);

        TextView rowText = new TextView(this);
        rowText.setText("- Can make " + String.valueOf(rows) + " full rows.");
        rowText.setTextSize(16);
        rowText.setTop(9 * x);
        rowText.setTextColor(android.graphics.Color.rgb(33,81,8));
        rowText.setGravity(Gravity.START);

        TextView columnText = new TextView(this);
        columnText.setText("- Can make " + String.valueOf(columns) + " full columns.");
        columnText.setTextSize(16);
        columnText.setTop(10 * x);
        columnText.setTextColor(android.graphics.Color.rgb(33,81,8));
        columnText.setGravity(Gravity.START);

        TextView relicText = new TextView(this);
        relicText.setText("- Can place " + String.valueOf(relics) + " relics.");
        relicText.setTextSize(16);
        relicText.setTop(11 * x);
        relicText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicText.setGravity(Gravity.START);

        TextView relicZoneText = new TextView(this);
        relicZoneText.setText("- Can place the relics in zone " + String.valueOf(relicZone) + ".");
        relicZoneText.setTextSize(16);
        relicZoneText.setTop(12 * x);
        relicZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicZoneText.setGravity(Gravity.START);

        TextView uprightText = new TextView(this);
        uprightText.setText("- " + returnCanOrCant(canUpright) + " place the relics upright.");
        uprightText.setTextSize(16);
        uprightText.setTop(13 * x);
        uprightText.setTextColor(android.graphics.Color.rgb(33,81,8));
        uprightText.setGravity(Gravity.START);

        TextView infoText = new TextView(this);
        infoText.setText("Other notes: " + teamInfo);
        infoText.setTextSize(16);
        infoText.setTop(14 * x);
        infoText.setTextColor(android.graphics.Color.rgb(33,81,8));
        infoText.setGravity(Gravity.START);

        llayout.addView(teamNameText);
        llayout.addView(teamNumText);
        llayout.addView(autonomous);
        llayout.addView(canJewelText);
        llayout.addView(canGlyphAutoText);
        llayout.addView(canAutoCypherText);
        llayout.addView(canSafeZoneText);
        llayout.addView(teleop);
        llayout.addView(glyphText);
        llayout.addView(rowText);
        llayout.addView(columnText);
        llayout.addView(relicText);
        llayout.addView(relicZoneText);
        llayout.addView(uprightText);
        llayout.addView(infoText);


    }

    public String returnCanOrCant(boolean b) {

        if(b) {

            return "Can";

        } else {

            return "Can not";

        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        mDb.close();
        llayout.removeAllViews();

    }

    public void editTeamDetails(int teamId) {

        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra("TEAM_NUMBER", teamId);
        startActivity(intent);

    }

}
