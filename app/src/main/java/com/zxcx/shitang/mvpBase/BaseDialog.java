package com.zxcx.shitang.mvpBase;

import android.app.DialogFragment;
import android.widget.Toast;

import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.utils.Constants;

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

/**
 * Created by anm on 2017/9/11.
 */

public class BaseDialog extends DialogFragment{

    protected Disposable mDisposable;
    //    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeDisposable mCompositeSubscription = null;

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

                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {

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
                                                return result;
                                            } else {
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
                                                return result.getData();
                                            } else {
                                                throw new Exception(result.getCode() + result.getMessage());
                                            }
                                        }
                                    }

                );
            }
        };
    }

    public void toastShow(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        super.onDestroy();
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
