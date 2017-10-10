package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.utils.Constants;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by anm on 2017/7/24.
 */

public abstract class NullPostSubscriber<T> extends DisposableSubscriber<T> {

    private INullPostPresenter mPresenter;

    public NullPostSubscriber(INullPostPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable t) {
        if (t.getMessage() != null) {
            String code = t.getMessage().substring(0,3);
            String message = t.getMessage().substring(3);
            if (String.valueOf(Constants.TOKEN_OUTTIME).equals(code)){
                mPresenter.startLogin();
            }else {
                mPresenter.postFail(message);
            }
        } else {
            mPresenter.postFail(App.getContext().getString(R.string.network_error));
        }
    }

    @Override
    public void onComplete() {

    }
}
