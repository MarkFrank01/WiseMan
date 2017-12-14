package com.zxcx.zhizhe.ui.my.creation.newCreation;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class RichTextEditorPresenter extends BasePresenter<RichTextEditorContract.View> implements RichTextEditorContract.Presenter {

    private final RichTextEditorModel mModel;

    public RichTextEditorPresenter(@NonNull RichTextEditorContract.View view) {
        attachView(view);
        mModel = new RichTextEditorModel(this);
    }

    @Override
    public void getDataSuccess(RichTextEditorBean bean) {
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

