package com.zxcx.shitang.ui.home.hot;

import com.zxcx.shitang.ui.home.hot.HotContract;
import com.zxcx.shitang.ui.home.hot.HotModel;
import com.zxcx.shitang.mvpBase.BasePresenter;

import android.support.annotation.NonNull;

public class HotPresenter extends BasePresenter<HotContract.View> implements HotContract.Presenter {

    private final HotModel mModel;

    public HotPresenter(@NonNull HotContract.View view) {
        attachView(view);
        mModel = new HotModel(this);
    }

    @Override
    public void getDataSuccess(HotBean bean) {
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

