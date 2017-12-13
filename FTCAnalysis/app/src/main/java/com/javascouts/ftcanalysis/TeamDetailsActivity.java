package com.javascouts.ftcanalysis;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
        MenuItem toHide = menu.findItem(R.id.action_export);
        toHide.setVisible(false);
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

        TextView teamNameText = new TextView(this);
        teamNameText.setText(teamName);
        teamNameText.setTextSize(32);
        teamNameText.setTop(0);
        teamNameText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNameText.setGravity(Gravity.CENTER);

        TextView teamNumText = new TextView(this);
        teamNumText.setText(String.valueOf(teamNumber));
        teamNumText.setTextSize(24);
        teamNumText.setTop(36);
        teamNumText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNumText.setGravity(Gravity.CENTER);

        TextView canJewelText = new TextView(this);
        canJewelText.setText(returnCanOrCant(canJewel) + " knock the correct jewel off.");
        canJewelText.setTextSize(16);
        canJewelText.setTop(72);
        canJewelText.setLeft(16);
        canJewelText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canJewelText.setGravity(Gravity.START);

        TextView canGlyphAutoText = new TextView(this);
        canGlyphAutoText.setText(returnCanOrCant(canGlyphAuto) + " place the glyph in the box.");
        canGlyphAutoText.setTextSize(16);
        canGlyphAutoText.setTop(90);
        canGlyphAutoText.setLeft(16);
        canGlyphAutoText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canGlyphAutoText.setGravity(Gravity.START);

        TextView canAutoCypherText = new TextView(this);
        canAutoCypherText.setText(returnCanOrCant(canCypher) + " place the glyph in the correct column.");
        canAutoCypherText.setTextSize(16);
        canAutoCypherText.setTop(108);
        canAutoCypherText.setLeft(16);
        canAutoCypherText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canAutoCypherText.setGravity(Gravity.START);

        TextView canSafeZoneText = new TextView(this);
        canSafeZoneText.setText(returnCanOrCant(canSafeZone) + " park in the safe zone.");
        canSafeZoneText.setTextSize(16);
        canSafeZoneText.setTop(126);
        canSafeZoneText.setLeft(16);
        canSafeZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canSafeZoneText.setGravity(Gravity.START);

        TextView glyphText = new TextView(this);
        glyphText.setText("Can score " + String.valueOf(glyphs) + " glyphs.");
        glyphText.setTextSize(16);
        glyphText.setTop(126);
        glyphText.setLeft(16);
        glyphText.setTextColor(android.graphics.Color.rgb(33,81,8));
        glyphText.setGravity(Gravity.START);

        TextView rowText = new TextView(this);
        rowText.setText("Can make " + String.valueOf(rows) + " full rows.");
        rowText.setTextSize(16);
        rowText.setTop(144);
        rowText.setLeft(16);
        rowText.setTextColor(android.graphics.Color.rgb(33,81,8));
        rowText.setGravity(Gravity.START);

        TextView columnText = new TextView(this);
        columnText.setText("Can make " + String.valueOf(columns) + " full columns.");
        columnText.setTextSize(16);
        columnText.setTop(162);
        columnText.setLeft(16);
        columnText.setTextColor(android.graphics.Color.rgb(33,81,8));
        columnText.setGravity(Gravity.START);

        TextView relicText = new TextView(this);
        relicText.setText("Can place " + String.valueOf(relics) + " relics.");
        relicText.setTextSize(16);
        relicText.setTop(180);
        relicText.setLeft(16);
        relicText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicText.setGravity(Gravity.START);

        TextView relicZoneText = new TextView(this);
        relicZoneText.setText("Can place the relics in zone " + String.valueOf(relicZone) + ".");
        relicZoneText.setTextSize(16);
        relicZoneText.setTop(198);
        relicZoneText.setLeft(16);
        relicZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicZoneText.setGravity(Gravity.START);

        TextView uprightText = new TextView(this);
        uprightText.setText(returnCanOrCant(canUpright) + " place the relics upright.");
        uprightText.setTextSize(16);
        uprightText.setTop(214);
        uprightText.setLeft(16);
        uprightText.setTextColor(android.graphics.Color.rgb(33,81,8));
        uprightText.setGravity(Gravity.START);

        llayout.addView(teamNameText);
        llayout.addView(teamNumText);
        llayout.addView(canJewelText);
        llayout.addView(canGlyphAutoText);
        llayout.addView(canAutoCypherText);
        llayout.addView(canSafeZoneText);
        llayout.addView(glyphText);
        llayout.addView(rowText);
        llayout.addView(columnText);
        llayout.addView(relicText);
        llayout.addView(relicZoneText);
        llayout.addView(uprightText);

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

    }

}
