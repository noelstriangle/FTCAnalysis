package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.javascouts.ftcanalysis.Team;

import java.util.List;

/**
 * Created by seed on 12/5/17.
 */

@Dao
public interface TeamDao {

    @Query("SELECT * FROM team")
    List<Team> getAll();

    @Query("SELECT teamNumber from team ORDER BY teamNumber")
    int[] getTeamNumbers();

    @Query("SELECT team_name from team ORDER BY teamNumber")
    String[] getTeamNames();

    @Query("SELECT auto_points from team ORDER BY teamNumber")
    int[] getAutoPoints();

    @Query("SELECT tele_points from team ORDER BY teamNumber")
    int[] getTelePoints();

    @Query("SELECT * FROM team WHERE teamNumber = :teamNumber")
    Team getTeam(int teamNumber);

    @Insert
    void insertAll(Team... teams);

}