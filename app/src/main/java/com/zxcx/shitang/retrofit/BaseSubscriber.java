package com.zxcx.shitang.retrofit;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by anm on 2017/5/31.
 */

public class BaseSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
