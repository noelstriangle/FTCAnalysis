package com.javascouts.ftcanalysis;

/**
 * Created by seed on 12/5/17.
 */
import android.arch.persistence.room.*;

@Entity
public class Team {

    @PrimaryKey
    private int teamNumber;

    @ColumnInfo(name="team_name")
    private String teamName;

    @ColumnInfo(name="auto_points")
    private int autoPoints;

    @ColumnInfo(name="tele_points")
    private int telePoints;

    public int getTeamNumber() {                    return this.teamNumber;}
    public void setTeamNumber(int value) {          this.teamNumber = value;}

    public String getTeamName() {                   return this.teamName;}
    public void setTeamName(String value) {         this.teamName = value;}

    public int getAutoPoints() {                    return this.autoPoints;}
    public void setAutoPoints(int value) {          this.autoPoints = value;}

    public int getTelePoints() {                    return this.telePoints;}
    public void setTelePoints(int value) {          this.telePoints = value;}


}
