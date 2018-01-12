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

public class MatchAdapter extends ArrayAdapter<Match> {

    public MatchAdapter(Context context, int textViewResourceId) {

        super(context, textViewResourceId);

    }

    public MatchAdapter(Context context, int resource, List<Match> items) {

        super(context, resource, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.content_match_row, null);

        }

        Match p = getItem(position);

        if (p != null) {

            TextView tt1 = v.findViewById(R.id.bTV);
            TextView tt2 = v.findViewById(R.id.b2TV);
            TextView tt3 = v.findViewById(R.id.rTV);
            TextView tt4 = v.findViewById(R.id.r2TV);
            TextView tt5 = v.findViewById(R.id.mN);

            if (tt1 != null) {
                tt1.setText(String.valueOf(p.getBlue1()));
            }

            if (tt2 != null) {
                tt2.setText(String.valueOf(p.getBlue2()));
            }

            if (tt3 != null) {
                tt3.setText(String.valueOf(p.getRed1()));
            }
            if (tt3 != null) {
                tt4.setText(String.valueOf(p.getRed2()));
            }
            if(tt5 != null) {
                tt5.setText(String.valueOf(p.getMatchNumber()));
            }
        }

        return v;
    }

}
