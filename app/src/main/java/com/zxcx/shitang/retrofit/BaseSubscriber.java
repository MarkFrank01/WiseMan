package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.App;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.IBasePresenter;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by anm on 2017/7/24.
 */

public abstract class BaseSubscriber<T> extends DisposableSubscriber<T> {

    private IBasePresenter mPresenter;

    public BaseSubscriber(IBasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable t) {
        mPresenter.getDataFail(App.getContext().getString(R.string.network_error));
    }

    @Override
    public void onComplete() {

    }
}
