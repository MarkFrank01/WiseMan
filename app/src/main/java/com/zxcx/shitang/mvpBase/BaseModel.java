package com.zxcx.shitang.mvpBase;


import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.utils.Constants;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class BaseModel<T extends IBasePresenter> {
    public Disposable subscription;
//    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeDisposable mCompositeSubscription = null;

    public T mPresent;

    public <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public <T> ObservableTransformer<T, T> io_main_loading() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                mPresent.showLoading();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                mPresent.hideLoading();
                            }
                        });
            }
        };
    }

    public <T> ObservableTransformer<BaseBean<T>, T> handleResult() {
        return new ObservableTransformer<BaseBean<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseBean<T>> upstream) {
                return upstream.map(new Function<BaseBean<T>, T>() {
                                        @Override
                                        public T apply(@NonNull BaseBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK.equals(result.getSuccess())) {
                                                return result.getData();
                                            } else if (Constants.RESULT_FAIL.equals(result.getSuccess())) {
                                                mPresent.getDataFail(result.getMessage());
                                            } else {

                                            }
                                            throw new Exception();
                                        }
                                    }

                );
            }
        };
    }

    public <T> ObservableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return new ObservableTransformer<BaseArrayBean<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(@NonNull Observable<BaseArrayBean<T>> upstream) {
                return upstream.map(new Function<BaseArrayBean<T>, List<T>>() {
                                        @Override
                                        public List<T> apply(@NonNull BaseArrayBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK.equals(result.getSuccess())) {
                                                return result.getData();
                                            } else if (Constants.RESULT_FAIL.equals(result.getSuccess())) {
                                                mPresent.getDataFail(result.getMessage());
                                            } else {

                                            }
                                            throw new Exception();
                                        }
                                    }

                );
            }
        };
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
