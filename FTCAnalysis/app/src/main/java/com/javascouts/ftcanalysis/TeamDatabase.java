package com.javascouts.ftcanalysis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by seed on 12/5/17.
 */

@Database(entities = {Team.class}, version = 3, exportSchema = false)
public abstract class TeamDatabase extends RoomDatabase {

    private static TeamDatabase INSTANCE;

    public abstract TeamDao TeamDao();

    public static TeamDatabase getTeamDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TeamDatabase.class, "team-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {

        INSTANCE = null;

    }

    public TeamDao getTeamDao() {

        return TeamDao();

    }

}
