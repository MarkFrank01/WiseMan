package com.zxcx.shitang.mvpBase;


import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.LogCat;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class BaseModel<T extends IBasePresenter> {
    protected Disposable subscription;
//    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeDisposable mCompositeSubscription = null;

    public T mPresent;

    protected <T> FlowableTransformer<T, T> io_main() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    protected <T> FlowableTransformer<T, T> io_main_loading() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull Subscription subscription) throws Exception {
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

    protected <T> FlowableTransformer<BaseBean<T>, T> handleResult() {
        return new FlowableTransformer<BaseBean<T>, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<BaseBean<T>> upstream) {
                return upstream.map(new Function<BaseBean<T>, T>() {
                                        @Override
                                        public T apply(@NonNull BaseBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                return result.getData();
                                            } else {
                                                LogCat.d("wang1");
                                                throw new Exception(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    protected FlowableTransformer<BaseBean, BaseBean> handlePostResult() {
        return new FlowableTransformer<BaseBean, BaseBean>() {
            @Override
            public Publisher<BaseBean> apply(@NonNull Flowable<BaseBean> upstream) {
                return upstream.map(new Function<BaseBean, BaseBean>() {
                                        @Override
                                        public BaseBean apply(@NonNull BaseBean result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                LogCat.d("wang");
                                                return result;
                                            } else {
                                                LogCat.d("wang2");
                                                throw new Exception(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    protected <T> FlowableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return new FlowableTransformer<BaseArrayBean<T>, List<T>>() {
            @Override
            public Publisher<List<T>> apply(@NonNull Flowable<BaseArrayBean<T>> upstream) {
                return upstream.map(new Function<BaseArrayBean<T>, List<T>>() {
                                        @Override
                                        public List<T> apply(@NonNull BaseArrayBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK == result.getCode()) {
                                                LogCat.d("好歹数据解析成功了");
                                                return result.getData();
                                            } else {
                                                LogCat.d("wang3");
                                                throw new Exception(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    public void onDestroy() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();//取消注册，以避免内存泄露
        }
    }

    protected void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
    }
}
