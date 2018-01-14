package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.javascouts.ftcanalysis.Team;

import java.sql.Blob;
import java.util.List;

/**
 * Created by seed on 12/5/17.
 */

@Dao
public interface TeamDao {

    @Query("SELECT * FROM teams")
    List<Team> getAll();

    @Query("SELECT * FROM teams ORDER BY team_number")
    List<Team> getAllAndSort();

    @Query("SELECT * FROM matches ORDER BY match_number")
    List<Match> getMatchesAndSort();

    @Query("SELECT * FROM teams WHERE id = :id")
    Team getTeam(int id);

    @Query("SELECT * FROM teams WHERE team_number = :tN")
    Team getTeamByTeamNumber(int tN);

    @Query("SELECT * FROM matches WHERE match_number = :mN")
    Match getMatchByMatchNumber(int mN);

    @Query("SELECT id FROM teams WHERE team_number = :tN")
    int getIdByTeamNumber(int tN);

    @Insert
    void insertAll(Team... teams);

    @Insert
    void insertMatch(Match... matches);

    @Update
    void updateAll(Team... teams);

    @Delete
    void deleteAll(Team... teams);

    @Delete
    void deleteMatch(Match... matches);

}