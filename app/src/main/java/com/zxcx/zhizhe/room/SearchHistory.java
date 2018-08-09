package com.zxcx.zhizhe.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.zxcx.zhizhe.retrofit.RetrofitBean;

/**
 * Created by anm on 2017/11/30.
 * 搜索历史表
 */

@Entity
public class SearchHistory extends RetrofitBean {
	
	@PrimaryKey
	@NonNull
	private String keyword;
	
	public SearchHistory(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		SearchHistory that = (SearchHistory) o;
		
		return keyword.equals(that.keyword);
	}
	
	@Override
	public int hashCode() {
		return keyword.hashCode();
	}
}
