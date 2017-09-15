package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.mvpBase.IGetPresenter;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.LogCat;

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
        String code = t.getMessage().substring(0,3);
        String message = t.getMessage().substring(1);
        t.printStackTrace();
        LogCat.d(t.getMessage());
        if (String.valueOf(Constants.TOKEN_OUTTIME).equals(code)){
            mPresenter.startLogin();
        }else {
            mPresenter.getDataFail(message);
        }
    }

    @Override
    public void onComplete() {

    }
}
