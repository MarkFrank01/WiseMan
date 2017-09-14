package com.zxcx.shitang.ui.search.result;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

import java.util.List;

public class SearchResultPresenter extends BasePresenter<SearchResultContract.View> implements SearchResultContract.Presenter {

    private final SearchResultModel mModel;

    public SearchResultPresenter(@NonNull SearchResultContract.View view) {
        attachView(view);
        mModel = new SearchResultModel(this);
    }

    public void searchCard(String keyword, int page, int pageSize){
        mModel.searchCard(keyword,page, pageSize);
    }

    public void searchCardBag(String keyword){
        mModel.searchCardBag(keyword);
    }

    @Override
    public void getDataSuccess(List<SearchCardBean> bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void searchCardBagSuccess(List<SearchCardBagBean> list) {
        mView.searchCardBagSuccess(list);
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

