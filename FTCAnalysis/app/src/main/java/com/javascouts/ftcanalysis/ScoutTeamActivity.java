package com.javascouts.ftcanalysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;

public class ScoutTeamActivity extends AppCompatActivity {

    private SeekBar glyphBar;
    private TextView glyphText;
    private EditText teamText, teamNum;
    private String team;
    private int teamNumber;

    teamCalc calc = new teamCalc();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_team);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(0,155,25));

        glyphText = (TextView)findViewById(R.id.glyphText);
        glyphText.setText("0");

        teamText = (EditText)findViewById(R.id.teamName);
        teamNum = (EditText)findViewById(R.id.teamNumber);

        glyphBar = (SeekBar)findViewById(R.id.glyphBar);
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



    }


    public void getTeamInfo() {

        team = String.valueOf(teamText.getText());
        teamNumber = Integer.valueOf(String.valueOf(teamNum.getText()));
        setTeamInfo();

    }

    public void setTeamInfo() {

        calc.teams[0] = teamNumber;

    }

}

