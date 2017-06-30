package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;

public class CollectFolderPresenter extends BasePresenter<CollectFolderContract.View> implements CollectFolderContract.Presenter {

    private final CollectFolderModel mModel;

    public CollectFolderPresenter(@NonNull CollectFolderContract.View view) {
        attachView(view);
        mModel = new CollectFolderModel(this);
    }

    @Override
    public void getDataSuccess(CollectFolderBean bean) {
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

