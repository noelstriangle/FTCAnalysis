package com.javascouts.ftcanalysis;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Liam on 12/9/2017.
 */

public class MatchDetailsActivity extends AppCompatActivity {

    TeamDatabase mDb;
    TeamDao mDao;
    Match match;
    int matchNumber;
    Team r1, r2, b1, b2;
    LinearLayout rlayout, blayout;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstnceState) {

        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_match_details);

        t = Toast.makeText(this, "Delete Cancelled.", Toast.LENGTH_SHORT);

        matchNumber = getIntent().getIntExtra("MATCH_NUMBER", 0);

        Toolbar myToolbar = findViewById(R.id.toolbarST);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Match Details");

        actionBar.setDisplayHomeAsUpEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();

                match = mDao.getMatchByMatchNumber(matchNumber);

                r1 = mDao.getTeamByTeamNumber(match.getRed1());
                r2 = mDao.getTeamByTeamNumber(match.getRed2());
                b1 = mDao.getTeamByTeamNumber(match.getBlue1());
                b2 = mDao.getTeamByTeamNumber(match.getBlue2());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        init();

                    }
                });

            }
        }).start();


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        MenuItem toHide1 = menu.findItem(R.id.action_export);
        toHide1.setVisible(false);
        MenuItem toHide2 = menu.findItem(R.id.action_deleteall);
        toHide2.setVisible(false);
        toHide1 = menu.findItem(R.id.action_settings);
        toHide1.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                break;

            case R.id.action_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Delete match " + String.valueOf(matchNumber) + " forever?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Match toDelete = mDao.getMatchByMatchNumber(matchNumber);
                                mDao.deleteMatch(toDelete);
                            }
                        }).start();

                        finish();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        t.show();

                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);

                break;

            case R.id.action_edit:


                break;


        }

        return true;

    }

    private void init() {

        rlayout = findViewById(R.id.rlayout);
        blayout = findViewById(R.id.blayout);

        TextView title = new TextView(this);
        title.setText(makeAString(r1, r2, -1));
        title.setTextColor(getResources().getColor(R.color.textColor2));
        title.setTextSize(36);
        title.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView number = new TextView(this);
        number.setText(makeAString(r1, r2, -2));
        number.setTextColor(getResources().getColor(R.color.textColor2));
        number.setTextSize(30);
        number.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView autonomous = new TextView(this);
        autonomous.setText(makeAString(r1, r2, -3));
        autonomous.setTextColor(getResources().getColor(R.color.textColor2));
        autonomous.setTextSize(20);
        autonomous.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView autonomous2 = new TextView(this);

        autonomous2.setText(makeAString(r1, r2, -3));
        autonomous2.setTextColor(getResources().getColor(R.color.textColor2));
        autonomous2.setTextSize(20);
        autonomous2.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView autoJewel = new TextView(this);
        autoJewel.setText(makeAString(r1, r2, 1));
        autoJewel.setTextColor(getResources().getColor(R.color.textColor2));
        autoJewel.setTextSize(16);

        TextView autoGlyph = new TextView(this);
        autoGlyph.setText(makeAString(r1, r2, 2));
        autoGlyph.setTextColor(getResources().getColor(R.color.textColor2));
        autoGlyph.setTextSize(16);

        TextView autoCypher = new TextView(this);
        autoCypher.setText(makeAString(r1, r2, 3));
        autoCypher.setTextColor(getResources().getColor(R.color.textColor2));
        autoCypher.setTextSize(16);

        TextView autoPark = new TextView(this);
        autoPark.setText(makeAString(r1, r2, 4));
        autoPark.setTextColor(getResources().getColor(R.color.textColor2));
        autoPark.setTextSize(16);

        TextView title2 = new TextView(this);
        title2.setText(makeAString(b1, b2, -1));
        title2.setTextColor(getResources().getColor(R.color.textColor2));
        title2.setTextSize(36);
        title2.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView number2 = new TextView(this);
        number2.setText(makeAString(b1, b2, -2));
        number2.setTextColor(getResources().getColor(R.color.textColor2));
        number2.setTextSize(30);
        number2.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView autoJewel2 = new TextView(this);
        autoJewel2.setText(makeAString(b1, b2, 1));
        autoJewel2.setTextColor(getResources().getColor(R.color.textColor2));
        autoJewel2.setTextSize(16);

        TextView autoGlyph2 = new TextView(this);
        autoGlyph2.setText(makeAString(b1, b2, 2));
        autoGlyph2.setTextColor(getResources().getColor(R.color.textColor2));
        autoGlyph2.setTextSize(16);

        TextView autoCypher2 = new TextView(this);
        autoCypher2.setText(makeAString(b1, b2, 3));
        autoCypher2.setTextColor(getResources().getColor(R.color.textColor2));
        autoCypher2.setTextSize(16);

        TextView autoPark2 = new TextView(this);
        autoPark2.setText(makeAString(b1, b2, 4));
        autoPark2.setTextColor(getResources().getColor(R.color.textColor2));
        autoPark2.setTextSize(16);

        rlayout.addView(title);
        rlayout.addView(number);
        rlayout.addView(autonomous);
        rlayout.addView(autoJewel);
        rlayout.addView(autoGlyph);
        rlayout.addView(autoCypher);
        rlayout.addView(autoPark);

        blayout.addView(title2);
        blayout.addView(number2);
        blayout.addView(autonomous2);
        blayout.addView(autoJewel2);
        blayout.addView(autoGlyph2);
        blayout.addView(autoCypher2);
        blayout.addView(autoPark2);

    }

    private String makeAString(Team t1, Team t2, int value) {

        String s = "";

        if (value == -1) {

            s = t1.getTeamName() + " and " + t2.getTeamName();

        } else if (value == -2) {

            s = String.valueOf(t1.getTeamNumber()) + " and " + String.valueOf(t2.getTeamNumber());

        } else if (value == -3) {

            s = "Autonomous";

        } else if (value == -4) {

            s = "TeleOp";

        } else if (value == 1) {

            if (t1.getJewelb() && t2.getJewelb()) {

                s = " - Both teams can knock their jewels off in autonomous.";

            } else if (!t1.getJewelb() && !t2.getJewelb()) {

                s = " - Neither team can knock the jewels off in autonomous.";

            } else if (t1.getJewelb() || t2.getJewelb()) {

                if (t1.getJewelb()) {
                    s = " - Team " + String.valueOf(t1.getTeamNumber()) + " can knock the jewel off, but team " + String.valueOf(t2.getTeamNumber() + " can not.");
                } else {
                    s = " - Team " + String.valueOf(t2.getTeamNumber()) + " can knock the jewel off, but team " + String.valueOf(t1.getTeamNumber() + " can not.");
                }
            }
        } else if (value == 2) {

            if (t1.getGlyphAutob() && t2.getGlyphAutob()) {

                s = " - Both teams can place the glyph in autonomous.";

            } else if (!t1.getGlyphAutob() && !t2.getGlyphAutob()) {

                s = " - Neither team can place the glyph in autonomous.";

            } else if (t1.getGlyphAutob() || t2.getGlyphAutob()) {

                if (t1.getGlyphAutob()) {
                    s = " - Team " + String.valueOf(t1.getTeamNumber()) + " can place the glyph, but team " + String.valueOf(t2.getTeamNumber() + " can not.");
                } else {
                    s = " - Team " + String.valueOf(t2.getTeamNumber()) + " can place the glyph, but team " + String.valueOf(t1.getTeamNumber() + " can not.");
                }
            }
        } else if (value == 3) {

                if (t1.getAutoCypherb() && t2.getAutoCypherb()) {

                    s = " - Both teams place the glyph in the correct column in autonomous.";

                } else if (!t1.getAutoCypherb() && !t2.getAutoCypherb()) {

                    s = " - Neither team can place the glyph in the correct column in autonomous.";

                } else if (t1.getAutoCypherb() || t2.getAutoCypherb()) {

                    if (t1.getAutoCypherb()) {
                        s = " - Team " + String.valueOf(t1.getTeamNumber()) + " can place the glyph in the correct column, but team " + String.valueOf(t2.getTeamNumber() + " can not.");
                    } else {
                        s = " - Team " + String.valueOf(t2.getTeamNumber()) + " can place the glyph in the correct column, but team " + String.valueOf(t1.getTeamNumber() + " can not.");

                    }
                }

            } else if (value == 4) {

                if (t1.getSafeZoneb() && t2.getSafeZoneb()) {

                    s = " - Both teams can park in the safe zone in autonomous.";

                } else if (!t1.getSafeZoneb() && !t2.getSafeZoneb()) {

                    s = " - Neither team can park in the safe zone in autonomous.";

                } else if (t1.getSafeZoneb() || t2.getSafeZoneb()) {

                    if (t1.getSafeZoneb()) {
                        s = " - Team " + String.valueOf(t1.getTeamNumber()) + " can park in the safe zone, but team " + String.valueOf(t2.getTeamNumber() + " can not.");
                    } else {
                        s = " - Team " + String.valueOf(t2.getTeamNumber()) + " can park in the safe zone, but team " + String.valueOf(t1.getTeamNumber() + " can not.");
                    }
                }


            }
        return s;
    }
}
