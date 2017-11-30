package com.zxcx.zhizhe.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/11/30.
 */

@Entity
public class SearchHistory extends RetrofitBaen{

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
}
