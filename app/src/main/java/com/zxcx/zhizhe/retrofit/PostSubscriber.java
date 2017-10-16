package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;

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
        mPresenter.hideLoading();
        if (t.getMessage() != null) {
            String code = t.getMessage().substring(0,3);
            try {
                int code1 = Integer.parseInt(code);
                String message = t.getMessage().substring(3);
                t.printStackTrace();
                LogCat.d(t.getMessage());
                if (Constants.TOKEN_OUTTIME == code1) {
                    mPresenter.startLogin();
                } else {
                    mPresenter.postFail(message);
                }
            } catch (NumberFormatException e) {
                mPresenter.postFail(App.getContext().getString(R.string.network_error));
            }
        } else {
            mPresenter.postFail(App.getContext().getString(R.string.network_error));
        }
    }

    @Override
    public void onComplete() {

    }
}
