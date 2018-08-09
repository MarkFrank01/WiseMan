package com.zxcx.zhizhe.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by anm on 2017/11/30.
 * 操作搜索历史接口
 */

@Dao
public interface SearchHistoryDao {
	
	/**
	 * 返回Flowable，数据变化时会自动调用OnNext通知
	 */
	@Query("SELECT * FROM SearchHistory")
	Flowable<List<SearchHistory>> getFlowableAll();
	
	@Query("SELECT * FROM SearchHistory")
	List<SearchHistory> getAll();
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	List<Long> insertAll(SearchHistory... users);
	
	@Delete
	int delete(SearchHistory... history);
	
	@Delete
	int delete(List<SearchHistory> historyList);
	
	@Query("DELETE FROM SearchHistory")
	int deleteAll();
	
}
