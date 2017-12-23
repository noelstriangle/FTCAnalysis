package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EditTeamActivity extends AppCompatActivity {

    private SeekBar glyphBar, rowBar, columnBar, relicBar, relicZoneBar;
    private TextView glyphText, rowText, columnText, relicText, relicZoneText;
    private EditText teamText, teamNum, description;
    private CheckBox jewel, glyphAuto, safeZone, autoCypher, endGameCypher, upright, balance;

    private boolean jewelb, glyphAutob, autoCypherb, safeZoneb, endGameCypherb, uprightb, balanceb;
    private int glyphBari, rowBari, columnBari, relicBari, relicZoneBari, teamNumi, teamN;
    private String teamTexts;
    TeamDatabase db;
    TeamDao dao;
    private Team team, tempTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_team);

        db = Room.databaseBuilder(getApplicationContext(),
                TeamDatabase.class, "team-database").build();

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

        teamN = getIntent().getIntExtra("TEAM_NUMBER", 0);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Edit Team");

        actionBar.setDisplayHomeAsUpEnabled(true);

        jewel = findViewById(R.id.jewel);
        glyphAuto = findViewById(R.id.glyphAuto);
        autoCypher = findViewById(R.id.autoCypher);
        safeZone = findViewById(R.id.safeZone);

        endGameCypher = findViewById(R.id.endGameCypher);
        upright = findViewById(R.id.upright);
        balance = findViewById(R.id.endBalance);

        glyphText = findViewById(R.id.glyphText);
        glyphText.setText("0");

        teamText = findViewById(R.id.teamName);
        teamNum = findViewById(R.id.teamNumber);
        description = findViewById(R.id.description);

        glyphBar = findViewById(R.id.glyphBar);
        glyphBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                glyphText.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rowText = findViewById(R.id.rowText);
        columnText = findViewById(R.id.columnText);
        relicText = findViewById(R.id.relicText);
        relicZoneText = findViewById(R.id.relicZoneText);

        rowText.setText("0");
        columnText.setText("0");
        relicText.setText("0");
        relicZoneText.setText("0");

        rowBar = findViewById(R.id.rowBar);
        rowBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                rowText.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        columnBar = findViewById(R.id.columnBar);
        columnBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                columnText.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        relicBar = findViewById(R.id.relicBar);
        relicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                relicText.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        relicZoneBar = findViewById(R.id.relicZoneBar);
        relicZoneBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                relicZoneText.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addInfo(teamN);

    }

    @Override
    protected void onDestroy() {

        jewelb = FALSE;
        glyphAutob = FALSE;
        autoCypherb = FALSE;
        safeZoneb = FALSE;
        endGameCypherb = FALSE;
        uprightb = FALSE;
        glyphBari = 0;
        rowBari = 0;
        columnBari = 0;
        relicBari = 0;
        relicZoneBari = 0;
        teamNumi = 0;
        teamTexts = "";
        TeamDatabase.destroyInstance();
        super.onDestroy();

    }

    public void getInfoAndKill(android.view.View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                tempTeam = new Team();

                try {

                    teamTexts = teamText.getText().toString();
                    teamNumi = Integer.valueOf(teamNum.getText().toString());

                } catch(NumberFormatException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast toast = new Toast(EditTeamActivity.this).makeText(EditTeamActivity.this, "Team Number Unspecified", Toast.LENGTH_LONG);
                            toast.show();

                        }
                    });
                    return;

                }

                jewelb = jewel.isChecked();
                glyphAutob = glyphAuto.isChecked();
                safeZoneb = safeZone.isChecked();
                autoCypherb = autoCypher.isChecked();

                glyphBari = glyphBar.getProgress();
                rowBari = rowBar.getProgress();
                columnBari = columnBar.getProgress();

                endGameCypherb = endGameCypher.isChecked();
                relicBari = relicBar.getProgress();
                relicZoneBari = relicZoneBar.getProgress();
                uprightb = upright.isChecked();
                balanceb = balance.isChecked();

                tempTeam.setTeamNumber(teamNumi);
                tempTeam.setTeamName(teamTexts);
                tempTeam.setJewelb(jewelb);
                tempTeam.setGlyphAutob(glyphAutob);
                tempTeam.setSafeZoneb(safeZoneb);
                tempTeam.setAutoCypherb(autoCypherb);
                tempTeam.setGlyphBari(glyphBari);
                tempTeam.setRowBari(rowBari);
                tempTeam.setColumnBari(columnBari);
                tempTeam.setEndGameCypherb(endGameCypherb);
                tempTeam.setRelicBari(relicBari);
                tempTeam.setRelicZoneBari(relicZoneBari);
                tempTeam.setUprightb(uprightb);
                tempTeam.setBalanceb(balanceb);
                tempTeam.setOtherNotes(description.getText().toString());

                tempTeam.setAutoPoints((changeBoolToInt(jewelb) * 30) + (changeBoolToInt(glyphAutob) * 15) + (changeBoolToInt(autoCypherb) * 30) + (changeBoolToInt(safeZoneb) * 10));
                tempTeam.setTelePoints((glyphBari * 2) + (rowBari * 10) + (columnBari * 20) +
                        (changeBoolToInt(endGameCypherb) * 30) + (relicBari * (10 * (2 ^ relicZoneBari - 1)) + (changeBoolToInt(uprightb) * 15)) + (changeBoolToInt(balanceb) * 20));

                addTeam(db, tempTeam, teamN);

            }

        }).start();

        TeamDatabase.destroyInstance();

        finish();

        navigateUpTo(new Intent(this, MainActivity.class));

    }

    public int changeBoolToInt(boolean value) {

        if(value) {

            return 1;

        } else {

            return 0;

        }

    }

    private static void addTeam(final TeamDatabase db, Team team, int todelete) {

        db.TeamDao().deleteAll(db.TeamDao().getTeam(todelete));
        db.TeamDao().insertAll(team);
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    public void addInfo(final int id) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                team = db.TeamDao().getTeam(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        teamText.setText(team.getTeamName());
                        teamNum.setText(String.valueOf(team.getTeamNumber()));
                        jewel.setChecked(team.getJewelb());
                        glyphAuto.setChecked(team.getGlyphAutob());
                        autoCypher.setChecked(team.getAutoCypherb());
                        safeZone.setChecked(team.getSafeZoneb());
                        glyphBar.setProgress(team.getGlyphBari());
                        rowBar.setProgress(team.getRowBari());
                        columnBar.setProgress(team.getColumnBari());
                        endGameCypher.setChecked(team.getEndGameCypherb());
                        relicBar.setProgress(team.getRelicBari());
                        relicZoneBar.setProgress(team.getRelicZoneBari());
                        upright.setChecked(team.getUprightb());
                        balance.setChecked(team.getBalanceb());


                    }
                });

            }
        }).start();



    }

}
