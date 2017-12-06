package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by seed on 12/5/17.
 */

@Database(entities = {Team.class}, version = 1)
public abstract class TeamDatabase extends RoomDatabase {

    public abstract TeamDao teamDao();

}
