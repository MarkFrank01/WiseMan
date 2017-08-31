package com.zxcx.shitang.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;
import com.zxcx.shitang.utils.Constants;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

public class AttentionModel extends BaseModel<AttentionContract.Presenter> {

    public AttentionModel(@NonNull AttentionContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAttentionCard(int userId, int page, int pageSize){
        subscription = AppClient.getAPIService().getAttentionCard(userId, page,pageSize)
                .compose(this.<BaseArrayBean<HotCardBean>>io_main())
                .compose(this.<HotCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void getAttentionCardBag(int userId){
        subscription = AppClient.getAPIService().getAttentionCardBag(userId, 1,1000)
                .compose(this.<BaseArrayBean<HotCardBagBean>>io_main())
                .compose(this.<HotCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBagBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBagBean> list) {
                        mPresent.getHotCardBagSuccess(list);
                    }
                });
        addSubscription(subscription);
    }



    protected <T> FlowableTransformer<BaseArrayBean<T>, List<T>> handleArrayResult() {
        return new FlowableTransformer<BaseArrayBean<T>, List<T>>() {
            @Override
            public Publisher<List<T>> apply(@io.reactivex.annotations.NonNull Flowable<BaseArrayBean<T>> upstream) {
                return upstream.map(new Function<BaseArrayBean<T>, List<T>>() {
                                        @Override
                                        public List<T> apply(@io.reactivex.annotations.NonNull BaseArrayBean<T> result) throws Exception {
                                            if (Constants.RESULT_OK.equals(result.getCode())) {
                                                return result.getData();
                                            } else if ("2".equals(result.getCode())) {
                                                mPresent.getDataFail("未选择兴趣");
                                            } else if (Constants.RESULT_FAIL.equals(result.getCode())) {
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
}


