package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.javascouts.ftcanalysis.Team;

import java.util.List;

/**
 * Created by seed on 12/5/17.
 */

@Dao
public interface TeamDao {

    @Query("SELECT * FROM team")
    List<Team> getAll();

    @Query("SELECT * FROM team ORDER BY team_number")
    List<Team> getAllAndSort();

    @Query("SELECT * FROM team WHERE id = :id")
    Team getTeam(int id);

    @Insert
    void insertAll(Team... teams);

    @Update
    void updateAll(Team... teams);
    @Delete
    void deleteAll(Team... teams);

}