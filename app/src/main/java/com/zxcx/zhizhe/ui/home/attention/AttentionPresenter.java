package com.zxcx.zhizhe.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;

import java.util.List;

public class AttentionPresenter extends BasePresenter<AttentionContract.View> implements AttentionContract.Presenter {

    private final AttentionModel mModel;

    public AttentionPresenter(@NonNull AttentionContract.View view) {
        attachView(view);
        mModel = new AttentionModel(this);
    }

    public void getHotCard(int page, int pageSize){
        mModel.getAttentionCard(page,pageSize);
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        mView.getDataSuccess(list);
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

