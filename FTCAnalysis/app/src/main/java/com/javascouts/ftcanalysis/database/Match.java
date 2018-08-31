package com.javascouts.ftcanalysis.database;

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

    @ColumnInfo(name = "blue1id")
    private int blue1id;

    @ColumnInfo(name = "blue2id")
    private int blue2id;

    @ColumnInfo(name = "red1id")
    private int red1id;

    @ColumnInfo(name = "red2id")
    private int red2id;

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

    public int getBlue1id() {
        return blue1id;
    }

    public void setBlue1id(int blue1id) {
        this.blue1id = blue1id;
    }

    public int getBlue2id() {
        return blue2id;
    }

    public void setBlue2id(int blue2id) {
        this.blue2id = blue2id;
    }

    public int getRed1id() {
        return red1id;
    }

    public void setRed1id(int red1id) {
        this.red1id = red1id;
    }

    public int getRed2id() {
        return red2id;
    }

    public void setRed2id(int red2id) {
        this.red2id = red2id;
    }
}
