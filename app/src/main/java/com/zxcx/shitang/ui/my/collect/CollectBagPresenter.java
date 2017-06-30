package com.zxcx.shitang.ui.my.collect;

import com.zxcx.shitang.ui.my.collect.CollectBagContract;
import com.zxcx.shitang.ui.my.collect.CollectBagModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class CollectBagPresenter extends BasePresenter<CollectBagContract.View> implements CollectBagContract.Presenter {

    private final CollectBagModel mModel;

    public CollectBagPresenter(@NonNull CollectBagContract.View view) {
        attachView(view);
        mModel = new CollectBagModel(this);
    }

    @Override
    public void getDataSuccess(CollectBagBean bean) {
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

