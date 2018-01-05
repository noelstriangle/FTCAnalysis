package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.Boolean.TRUE;

public class FirstRunActivity extends AppCompatActivity {

    private SeekBar glyphBar, rowBar, columnBar, relicBar, relicZoneBar;
    private TextView glyphText, rowText, columnText, relicText, relicZoneText;
    private EditText teamText, teamNum, description;
    private CheckBox jewel, glyphAuto, safeZone, autoCypher, endGameCypher, upright, balance, onFTC;
    private View scout;

    private boolean jewelb, glyphAutob, autoCypherb, safeZoneb, endGameCypherb, uprightb, balanceb;
    private int glyphBari, rowBari, columnBari, relicBari, relicZoneBari, teamNumi;
    private String teamTexts, teamInfos;
    TeamDatabase db;
    private Team tempTeam;
    private static final int SELECT_PHOTO = 100;
    private InputStream imageStream;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Setup");
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor2));

        db = Room.databaseBuilder(getApplicationContext(),
                TeamDatabase.class, "team-database").build();

        jewel = findViewById(R.id.jewel);
        glyphAuto = findViewById(R.id.glyphAuto);
        autoCypher = findViewById(R.id.autoCypher);
        safeZone = findViewById(R.id.safeZone);

        onFTC = findViewById(R.id.onFTC);
        scout = findViewById(R.id.scout);

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

        onFTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                    scout.setVisibility(View.VISIBLE);

                } else {

                    scout.setVisibility(View.INVISIBLE);

                }

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void selectPic(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        image = decodeUri(selectedImage);
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    public void getInfoAndKill(android.view.View view) {

        if(onFTC.isChecked()) {

            finish();

        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                tempTeam = new Team();

                try {

                    teamTexts = teamText.getText().toString();
                    teamNumi = Integer.valueOf(teamNum.getText().toString());
                    teamInfos = description.getText().toString();

                } catch(NumberFormatException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast toast = new Toast(FirstRunActivity.this).makeText(FirstRunActivity.this, "Team Number Unspecified", Toast.LENGTH_LONG);
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
                tempTeam.setOtherNotes(teamInfos);
                tempTeam.setIsUser(true);
                try {
                    tempTeam.setImage(getBytes(image));
                } catch(NullPointerException e) {
                    tempTeam.setImage(getBytes(BitmapFactory.decodeResource(getResources(), R.mipmap.no_image)));
                }

                tempTeam.setAutoPoints((changeBoolToInt(jewelb) * 30) + (changeBoolToInt(glyphAutob) * 15) + (changeBoolToInt(autoCypherb) * 30) + (changeBoolToInt(safeZoneb) * 10));
                tempTeam.setTelePoints((glyphBari * 2) + (rowBari * 10) + (columnBari * 20) +
                        (changeBoolToInt(endGameCypherb) * 30) + (relicBari * (10 * (2 ^ relicZoneBari - 1)) + (changeBoolToInt(uprightb) * 15)) + (changeBoolToInt(balanceb) * 20));

                addTeam(db, tempTeam);

            }

        }).start();

        TeamDatabase.destroyInstance();

        finish();

    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 520;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    public int changeBoolToInt(boolean value) {

        if(value) {

            return 1;

        } else {

            return 0;

        }

    }

    private static void addTeam(final TeamDatabase db, Team team) {

        db.TeamDao().insertAll(team);

    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        try {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch(NullPointerException e) {
            Bitmap b = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.no_image);
            return b;
        }
    }
}
