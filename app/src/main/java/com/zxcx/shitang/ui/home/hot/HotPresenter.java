package com.zxcx.shitang.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

import java.util.List;

public class HotPresenter extends BasePresenter<HotContract.View> implements HotContract.Presenter {

    private final HotModel mModel;

    public HotPresenter(@NonNull HotContract.View view) {
        attachView(view);
        mModel = new HotModel(this);
    }

    public void getHotCard(int page, int pageSize){
        mModel.getHotCard(page,pageSize);
    }

    public void getHotCardBag(){
        mModel.getHotCardBag();
    }

    @Override
    public void getDataSuccess(List<HotCardBean> s) {
        mView.getDataSuccess(s);
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {

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

