package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.App;
import com.zxcx.shitang.R;
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
        String code = t.getMessage().substring(0,1);
        String message = t.getMessage().substring(1);
        if (Constants.RESULT_FAIL.equals(code)){
            mPresenter.postFail(message);
        }else {
            mPresenter.postFail(App.getContext().getString(R.string.data_error));
        }
    }

    @Override
    public void onComplete() {

    }
}
