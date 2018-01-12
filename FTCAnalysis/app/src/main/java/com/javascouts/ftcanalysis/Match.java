package com.javascouts.ftcanalysis;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by seed on 1/1/18.
 */

@Entity(tableName = "matches")
public class Match {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "match_number")
    private int matchNumber;

    @ColumnInfo(name = "blue1")
    private int blue1;

    @ColumnInfo(name = "blue2")
    private int blue2;

    @ColumnInfo(name = "red1")
    private int red1;

    @ColumnInfo(name = "red2")
    private int red2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public int getBlue1() {
        return blue1;
    }

    public void setBlue1(int blue1) {
        this.blue1 = blue1;
    }

    public int getBlue2() {
        return blue2;
    }

    public void setBlue2(int blue2) {
        this.blue2 = blue2;
    }

    public int getRed1() {
        return red1;
    }

    public void setRed1(int red1) {
        this.red1 = red1;
    }

    public int getRed2() {
        return red2;
    }

    public void setRed2(int red2) {
        this.red2 = red2;
    }

}
