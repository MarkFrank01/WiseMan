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

    @Override
    public void getDataSuccess(SelectAttentionBean bean) {
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

    @Override
    public void selectAttention(List<String> cardBagId) {

    }
}
