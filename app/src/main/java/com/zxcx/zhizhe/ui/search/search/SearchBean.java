package com.zxcx.zhizhe.ui.search.search;

import java.util.List;

/**
 * Created by anm on 2018/3/20.
 */

public class SearchBean {
    private List<String> mHotSearchList;
    private List<String> mSearchHistoryList;

    public SearchBean(List<String> hotSearchList, List<String> searchHistoryList) {
        mHotSearchList = hotSearchList;
        mSearchHistoryList = searchHistoryList;
    }

    public List<String> getHotSearchList() {
        return mHotSearchList;
    }

    public void setHotSearchList(List<String> hotSearchList) {
        mHotSearchList = hotSearchList;
    }

    public List<String> getSearchHistoryList() {
        return mSearchHistoryList;
    }

    public void setSearchHistoryList(List<String> searchHistoryList) {
        mSearchHistoryList = searchHistoryList;
    }
}
