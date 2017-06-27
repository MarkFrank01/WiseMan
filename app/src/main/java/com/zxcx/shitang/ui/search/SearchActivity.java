package com.zxcx.shitang.ui.search;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends MvpActivity<SearchPresenter> implements SearchContract.View {

    private List<String> mSearchHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchHistoryList = getSearchHistory();
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void getDataSuccess(SearchBean bean) {

    }

    private List<String> getSearchHistory() {
        List<String> stringList = new ArrayList<>();
        String searchHistoryJson = SharedPreferencesUtil.getString(Constants.SEARCH_HISTORY, "");
        if (!StringUtils.isEmpty(searchHistoryJson)){
            stringList = JSON.parseArray(searchHistoryJson,String.class);
        }
        return stringList;
    }

    private void saveSearchHistory(String searchHistory) {
        mSearchHistoryList.add(searchHistory);
        if (mSearchHistoryList.size() > 6){
            mSearchHistoryList.remove(0);
        }
        if (mSearchHistoryList != null && !mSearchHistoryList.isEmpty()) {
            String searchHistoryJson = JSON.toJSONString(mSearchHistoryList);
            SharedPreferencesUtil.saveData(Constants.SEARCH_HISTORY,searchHistoryJson);
        }
    }
}
