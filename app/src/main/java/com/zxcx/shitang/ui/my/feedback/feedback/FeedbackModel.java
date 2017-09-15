package com.zxcx.shitang.ui.my.feedback.feedback;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.PostSubscriber;

public class FeedbackModel extends BaseModel<FeedbackContract.Presenter> {
    public FeedbackModel(@NonNull FeedbackContract.Presenter present) {
        this.mPresent = present;
    }

    public void feedback(String content, String contact,int appType, String appChannel, String appVersion){
        subscription = AppClient.getAPIService().feedback(content,contact,appType,appChannel,appVersion)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(mPresent) {
                    @Override
                    public void onNext(PostBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }
}


