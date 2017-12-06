package com.javascouts.ftcanalysis;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    teamCalc calc = new teamCalc();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(android.graphics.Color.rgb(0,155,25));

        //myToolbar.setLogo();

    }

    public void scoutNewTeam(View view) {

        Intent intent = new Intent(this, ScoutTeamActivity.class);
        startActivity(intent);

    }

    public void init() {

        TableLayout tbl = (TableLayout)findViewById(R.id.mainTable);
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

        for(int i = 0; i < calc.numberOfTeams; i++) {

            TableRow tblrow = new TableRow(this);

            TextView tv1 = new TextView(this);
            tv1.setText("" + calc.teams[i]);
            tv1.setTextColor(android.graphics.Color.rgb(0,155,25));
            tv1.setGravity(Gravity.CENTER);
            tblrow.addView(tv1);

            tbl.addView(tblrow);

        }

    }

}
