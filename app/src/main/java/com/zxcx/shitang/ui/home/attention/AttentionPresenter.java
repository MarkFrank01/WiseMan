package com.zxcx.shitang.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;

import java.util.List;

public class AttentionPresenter extends BasePresenter<AttentionContract.View> implements AttentionContract.Presenter {

    private final AttentionModel mModel;

    public AttentionPresenter(@NonNull AttentionContract.View view) {
        attachView(view);
        mModel = new AttentionModel(this);
    }

    public void getHotCard(int userId, int page, int pageSize){
        mModel.getAttentionCard(userId, page,pageSize);
    }

    public void getHotCardBag(int userId){
        mModel.getAttentionCardBag(userId);
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {

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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

