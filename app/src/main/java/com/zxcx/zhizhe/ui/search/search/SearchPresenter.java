package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.room.SearchHistory;

import java.util.List;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    private final SearchModel mModel;

    public SearchPresenter(@NonNull SearchContract.View view) {
        attachView(view);
        mModel = new SearchModel(this);
    }

    public void getSearchHot(){
        mModel.getSearchHot();
    }

    public void getSearchPre(String keyword){
        mModel.getSearchPre(keyword);
    }

    @Override
    public void getDataSuccess(List<SearchBean> bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getSearchHistorySuccess(List<SearchHistory> list) {
        mView.getSearchHistorySuccess(list);
    }

    @Override
    public void getSearchPreSuccess(List<String> list) {
        mView.getSearchPreSuccess(list);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    @Override
    public void showLoading() {
        mView.showLoading();
    }

    @Override
    public void hideLoading() {
        mView.hideLoading();
    }

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

