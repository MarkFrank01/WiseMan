package com.zxcx.shitang.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

import java.util.List;

public class SelectAttentionPresenter extends BasePresenter<SelectAttentionContract.View> implements SelectAttentionContract.Presenter {

    private final SelectAttentionModel mModel;

    public SelectAttentionPresenter(@NonNull SelectAttentionContract.View view) {
        attachView(view);
        mModel = new SelectAttentionModel(this);
    }

    public void getAttentionList(){
        mModel.getAttentionList();
    }

    public void changeAttentionList(List<Integer> idList){
        mModel.changeAttentionList(idList);
    }

    @Override
    public void getDataSuccess(List<SelectAttentionBean> bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    @Override
    public void postSuccess() {
        mView.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
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

