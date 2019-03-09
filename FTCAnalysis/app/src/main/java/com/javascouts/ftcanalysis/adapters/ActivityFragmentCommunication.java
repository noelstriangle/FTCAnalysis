package com.javascouts.ftcanalysis.adapters;

import com.javascouts.ftcanalysis.room.UserDao;
import com.javascouts.ftcanalysis.room.TeamDatabase;

public interface ActivityFragmentCommunication {

    TeamDatabase getDb();

    void setDb(TeamDatabase db);

    UserDao getDao();

    void setDao(UserDao dao);

    String getCurrent();

    void setCurrent(String current);

}
