package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.App;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.IGetPresenter;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by anm on 2017/7/24.
 */

public abstract class BaseSubscriber<T> extends DisposableSubscriber<T> {

    private IGetPresenter mPresenter;

    public BaseSubscriber(IGetPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable t) {
        mPresenter.getDataFail(App.getContext().getString(R.string.data_error));
    }

    @Override
    public void onComplete() {

    }
}
