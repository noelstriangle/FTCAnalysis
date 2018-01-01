package com.javascouts.ftcanalysis;

/**
 * Created by seed on 12/5/17.
 */
import android.arch.persistence.room.*;

import java.sql.Blob;

@Entity(tableName = "teams")
public class Team {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="team_number")
    private int teamNumber;

    @ColumnInfo(name="team_name")
    private String teamName;

    @ColumnInfo(name="auto_points")
    private int autoPoints;

    @ColumnInfo(name="tele_points")
    private int telePoints;

    @ColumnInfo(name="canJewel")
    private boolean jewelb;

    @ColumnInfo(name="canGlyph")
    private boolean glyphAutob;

    @ColumnInfo(name="canAutoCypher")
    private boolean autoCypherb;

    @ColumnInfo(name="canSafe")
    private boolean safeZoneb;

    @ColumnInfo(name="canCypher")
    private boolean endGameCypherb;

    @ColumnInfo(name="canUpright")
    private boolean uprightb;

    @ColumnInfo(name="canBalance")
    private boolean balanceb;

    @ColumnInfo(name="tele_glyphs")
    private int glyphBari;

    @ColumnInfo(name="tele_rows")
    private int rowBari;

    @ColumnInfo(name="tele_columns")
    private int columnBari;

    @ColumnInfo(name="tele_relics")
    private int relicBari;

    @ColumnInfo(name="tele_relic_zone")
    private int relicZoneBari;

    @ColumnInfo(name="other_notes")
    private String otherNotes;

    @ColumnInfo(name="image")
    private byte[] image;

    @ColumnInfo(name="isUser")
    private boolean isUser;

    public int getId() {                            return this.id;}
    public void setId(int value) {                  this.id = value;}

    public int getTeamNumber() {                    return this.teamNumber;}
    public void setTeamNumber(int value) {          this.teamNumber = value;}

    public String getTeamName() {                   return this.teamName;}
    public void setTeamName(String value) {         this.teamName = value;}

    public int getAutoPoints() {                    return this.autoPoints;}
    public void setAutoPoints(int value) {          this.autoPoints = value;}

    public int getTelePoints() {                    return this.telePoints;}
    public void setTelePoints(int value) {          this.telePoints = value;}

    public boolean getJewelb() {                  return this.jewelb;}
    public void setJewelb(boolean value) {        this.jewelb = value;}

    public boolean getGlyphAutob() {                  return this.glyphAutob;}
    public void setGlyphAutob(boolean value) {        this.glyphAutob = value;}

    public boolean getSafeZoneb() {                   return this.safeZoneb;}
    public void setSafeZoneb(boolean value) {         this.safeZoneb = value;}

    public boolean getEndGameCypherb() {                 return this.endGameCypherb;}
    public void setEndGameCypherb(boolean value) {       this.endGameCypherb = value;}

    public boolean getUprightb() {                return this.uprightb;}
    public void setUprightb(boolean value) {      this.uprightb = value;}

    public int getGlyphBari() {                    return this.glyphBari;}
    public void setGlyphBari(int value) {          this.glyphBari = value;}

    public int getRowBari() {                      return this.rowBari;}
    public void setRowBari(int value) {            this.rowBari = value;}

    public int getColumnBari() {                   return this.columnBari;}
    public void setColumnBari(int value) {         this.columnBari = value;}

    public int getRelicBari() {                    return this.relicBari;}
    public void setRelicBari(int value) {          this.relicBari = value;}

    public int getRelicZoneBari() {                 return this.relicZoneBari;}
    public void setRelicZoneBari(int value) {       this.relicZoneBari = value;}

    public boolean getAutoCypherb() {             return this.autoCypherb;}
    public void setAutoCypherb(boolean value) {   this.autoCypherb = value;}

    public boolean getBalanceb() {                  return this.balanceb;}
    public void setBalanceb(boolean value) {                     this.balanceb = value;}

    public String getOtherNotes() {                return this.otherNotes;}
    public void setOtherNotes(String value) {                   this.otherNotes=value;}

    public byte[] getImage() {                        return this.image;}
    public void setImage(byte[] value) {                            this.image = value;}

    public boolean getIsUser() {                       return isUser;}
    public void setIsUser(boolean value) {             isUser = value;}

}
