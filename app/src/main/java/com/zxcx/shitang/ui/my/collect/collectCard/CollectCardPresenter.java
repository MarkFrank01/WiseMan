package com.zxcx.shitang.ui.my.collect.collectCard;

import com.zxcx.shitang.ui.my.collect.collectCard.CollectCardContract;
import com.zxcx.shitang.ui.my.collect.collectCard.CollectCardModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class CollectCardPresenter extends BasePresenter<CollectCardContract.View> implements CollectCardContract.Presenter {

    private final CollectCardModel mModel;

    public CollectCardPresenter(@NonNull CollectCardContract.View view) {
        attachView(view);
        mModel = new CollectCardModel(this);
    }

    @Override
    public void getDataSuccess(CollectCardBean bean) {
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

