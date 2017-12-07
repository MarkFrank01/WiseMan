package com.zxcx.zhizhe.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by anm on 2017/11/30.
 */

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM SearchHistory")
    Flowable<List<SearchHistory>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(SearchHistory... users);

    @Delete
    void delete(SearchHistory... history);

    @Query("DELETE FROM SearchHistory")
    void deleteAll();

}
