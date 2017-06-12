package com.zxcx.shitang.mvpBase;


import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.utils.Constants;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BaseModel<T extends IBasePresenter> {
    public Disposable subscription;
//    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeDisposable mCompositeSubscription = null;

    public T mPresent;

    public <T> ObservableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> ObservableTransformer<T, T> io_main_loading() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mPresent.showLoading())
                .doOnTerminate(() -> mPresent.hideLoading());
    }

    public <T> ObservableTransformer<BaseBean<T>, T> handleResult() {
        return upstream ->
                upstream.map(result -> {
                    if (Constants.RESULT_OK.equals(result.getSuccess())) {
                        return result.getData();
                    } else if (Constants.RESULT_FAIL.equals(result.getSuccess())) {
                        mPresent.getDataFail(result.getMessage());
                    } else {

                    }
                    throw new Exception();
                }

        );
    }

    public <T> ObservableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return upstream ->
                upstream.map(result -> {
                    if (Constants.RESULT_OK.equals(result.getSuccess())) {
                        return result.getData();
                    } else if (Constants.RESULT_FAIL.equals(result.getSuccess())) {
                        mPresent.getDataFail(result.getMessage());
                    } else {

                    }
                    throw new Exception();
                }

        );
    }

    public void onDestroy() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
    }
}
