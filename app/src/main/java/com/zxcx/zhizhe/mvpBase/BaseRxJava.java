package com.zxcx.zhizhe.mvpBase;

import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.utils.Constants;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anm on 2017/9/11.
 */

public class BaseRxJava{

    public static <T> FlowableTransformer<T, T> io_main() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> io_main_loading(final IBasePresenter presenter) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull Subscription subscription) throws Exception {
                                presenter.showLoading();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                presenter.hideLoading();
                            }
                        });
            }
        };
    }

    public static <T> FlowableTransformer<BaseBean<T>, T> handleResult() {
        return new FlowableTransformer<BaseBean<T>, T>() {
            @Override
            public Publisher<T> apply(@NonNull final Flowable<BaseBean<T>> upstream) {
                return upstream.map(new Function<BaseBean<T>, T>() {
                                        @Override
                                        public T apply(@NonNull BaseBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                return result.getData();
                                            } else {
                                                throw new RuntimeException(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    public static FlowableTransformer<BaseBean, BaseBean> handlePostResult() {
        return new FlowableTransformer<BaseBean, BaseBean>() {
            @Override
            public Publisher<BaseBean> apply(@NonNull Flowable<BaseBean> upstream) {
                return upstream.map(new Function<BaseBean, BaseBean>() {
                                        @Override
                                        public BaseBean apply(@NonNull BaseBean result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                return result;
                                            } else {
                                                throw new RuntimeException(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    public static <T> FlowableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return new FlowableTransformer<BaseArrayBean<T>, List<T>>() {
            @Override
            public Publisher<List<T>> apply(@NonNull Flowable<BaseArrayBean<T>> upstream) {
                return upstream.map(new Function<BaseArrayBean<T>, List<T>>() {
                                        @Override
                                        public List<T> apply(@NonNull BaseArrayBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                return result.getData();
                                            } else {
                                                throw new RuntimeException(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }
}
