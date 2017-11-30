package com.zxcx.zhizhe.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.zxcx.zhizhe.App;

/**
 * Created by anm on 2017/11/30.
 */

@Database(entities = {SearchHistory.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SearchHistoryDao mSearchHistoryDao();

    private static class AppDatabaseHolder{
        private static final AppDatabase INSTANCE = Room.databaseBuilder(App.getContext(), AppDatabase.class, "zhizhe.db")
                .build();
    }

    public static AppDatabase getInstance() {
        return AppDatabaseHolder.INSTANCE;
    }
}
