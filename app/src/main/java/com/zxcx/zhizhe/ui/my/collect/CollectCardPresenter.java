package com.zxcx.zhizhe.ui.my.collect;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

import java.util.List;

public class CollectCardPresenter extends BasePresenter<CollectCardContract.View> implements CollectCardContract.Presenter {

    private final CollectCardModel mModel;

    public CollectCardPresenter(@NonNull CollectCardContract.View view) {
        attachView(view);
        mModel = new CollectCardModel(this);
    }

    public void getCollectCard(int sortType, int page, int pageSize){
        mModel.getCollectCard(sortType,page,pageSize);
    }

    @Override
    public void getDataSuccess(List<CollectCardBean> bean) {
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

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

