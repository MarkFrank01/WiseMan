package com.zxcx.shitang.ui.allCardBag;

import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class AllCardBagPresenter extends BasePresenter<AllCardBagContract.View> implements AllCardBagContract.Presenter {

    private final AllCardBagModel mModel;

    public AllCardBagPresenter(@NonNull AllCardBagContract.View view) {
        attachView(view);
        mModel = new AllCardBagModel(this);
    }

    @Override
    public void getDataSuccess(AllCardBagBean bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

