package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.mvpBase.IPostPresenter;
import com.zxcx.shitang.utils.Constants;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by anm on 2017/7/24.
 */

public abstract class PostSubscriber<T> extends DisposableSubscriber<T> {

    private IPostPresenter mPresenter;

    public PostSubscriber(IPostPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable t) {
        String code = t.getMessage().substring(0,3);
        String message = t.getMessage().substring(3);
        if (String.valueOf(Constants.TOKEN_OUTTIME).equals(code)){
            mPresenter.startLogin();
        }else {
            mPresenter.postFail(message);
        }
    }

    @Override
    public void onComplete() {

    }
}
