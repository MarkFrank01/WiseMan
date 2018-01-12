package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

public class UserSafetyModel extends BaseModel<UserSafetyContract.Presenter> {
    public UserSafetyModel(@NonNull UserSafetyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void channelBinding(int channelType, int type, String openId){
        mDisposable = AppClient.getAPIService().channelBinding(channelType,type,openId)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
    }
}


