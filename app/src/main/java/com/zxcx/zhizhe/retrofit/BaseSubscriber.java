package com.zxcx.zhizhe.retrofit;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;

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
        if (t.getMessage() != null) {
            String code = t.getMessage().substring(0, 3);
            try {
                int code1 = Integer.parseInt(code);
                String message = t.getMessage().substring(3);
                t.printStackTrace();
                LogCat.d(t.getMessage());
                if (Constants.TOKEN_OUTTIME == code1) {
                    mPresenter.startLogin();
                } else {
                    mPresenter.getDataFail(message);
                }
            } catch (NumberFormatException e) {
                mPresenter.getDataFail(App.getContext().getString(R.string.network_error));
            }
        }else {
            mPresenter.getDataFail(App.getContext().getString(R.string.network_error));
        }
    }

    @Override
    public void onComplete() {

    }
}
