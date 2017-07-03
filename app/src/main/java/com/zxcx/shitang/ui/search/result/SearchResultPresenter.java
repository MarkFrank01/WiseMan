package com.zxcx.shitang.ui.search.result;

import com.zxcx.shitang.ui.search.result.SearchResultContract;
import com.zxcx.shitang.ui.search.result.SearchResultModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class SearchResultPresenter extends BasePresenter<SearchResultContract.View> implements SearchResultContract.Presenter {

    private final SearchResultModel mModel;

    public SearchResultPresenter(@NonNull SearchResultContract.View view) {
        attachView(view);
        mModel = new SearchResultModel(this);
    }

    @Override
    public void getDataSuccess(SearchResultBean bean) {
        mView.getDataSuccess(bean);
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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

