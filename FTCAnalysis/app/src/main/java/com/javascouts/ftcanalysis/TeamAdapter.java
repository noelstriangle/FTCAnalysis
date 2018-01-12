package com.javascouts.ftcanalysis;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by seed on 12/29/17.
 */

public class TeamAdapter extends ArrayAdapter<Team> {

    public TeamAdapter(Context context, int textViewResourceId) {

        super(context, textViewResourceId);

    }

    public TeamAdapter(Context context, int resource, List<Team> items) {

        super(context, resource, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.content_row, null);

        }

        Team p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.titleText);
            TextView tt2 = v.findViewById(R.id.subTitleText);
            TextView tt3 = v.findViewById(R.id.pointsText);

            if(p.getIsUser()) {

                v.setBackgroundColor(Color.rgb(37, 130, 41));
                tt1.setTextColor(Color.rgb(255, 202, 43));
                tt2.setTextColor(Color.rgb(255,202,43));
                tt3.setTextColor(Color.rgb(255,202,43));

            }

            if (tt1 != null) {
                tt1.setText(p.getTeamName());
            }

            if (tt2 != null) {
                tt2.setText(String.valueOf(p.getTeamNumber()));
            }

            if (tt3 != null) {
                tt3.setText(String.valueOf(p.getTelePoints() + p.getAutoPoints()));
            }
        }

        return v;
    }

}
