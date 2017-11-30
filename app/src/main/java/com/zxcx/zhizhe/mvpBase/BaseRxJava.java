package com.zxcx.zhizhe.mvpBase;

import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.utils.Constants;

import java.util.List;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anm on 2017/9/11.
 */

public class BaseRxJava{

    public static <T> FlowableTransformer<T, T> io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> io_main_loading(final IBasePresenter presenter) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> presenter.showLoading())
                .doOnTerminate(() -> presenter.hideLoading())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<BaseBean<T>, T> handleResult() {
        return upstream -> upstream.map((Function<BaseBean<T>, T>) result -> {
            if (Constants.RESULT_OK == result.getCode()) {
                return result.getData();
            } else {
                throw new RuntimeException(result.getCode() + result.getMessage());
            }
        }

        );
    }

    public static FlowableTransformer<BaseBean, BaseBean> handlePostResult() {
        return upstream -> upstream.map((Function<BaseBean, BaseBean>) result -> {
            if (Constants.RESULT_OK == result.getCode()) {
                return result;
            } else {
                throw new RuntimeException(result.getCode() + result.getMessage());
            }
        }

        );
    }

    public static <T> FlowableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return upstream -> upstream.map((Function<BaseArrayBean<T>, List<T>>) result -> {
            if (Constants.RESULT_OK == result.getCode()) {
                return result.getData();
            } else {
                throw new RuntimeException(result.getCode() + result.getMessage());
            }
        }

        );
    }
}
