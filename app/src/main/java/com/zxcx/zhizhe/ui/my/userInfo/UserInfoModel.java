package com.zxcx.zhizhe.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.PostSubscriber;

public class UserInfoModel extends BaseModel<UserInfoContract.Presenter> {
    public UserInfoModel(@NonNull UserInfoContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getOSS(String uuid){
        mDisposable = AppClient.getAPIService().getOSS(uuid)
                .compose(BaseRxJava.<BaseBean<OSSTokenBean>>io_main_loading(mPresenter))
                .compose(BaseRxJava.<OSSTokenBean>handleResult())
                .subscribeWith(new BaseSubscriber<OSSTokenBean>(mPresenter) {
                    @Override
                    public void onNext(OSSTokenBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void changeImageUrl(String imageUrl){
        mDisposable = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null)
                .compose(BaseRxJava.<BaseBean<UserInfoBean>>io_main_loading(mPresenter))
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresenter) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
    }

    public void changeBirth(String birth){
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, birth)
                .compose(BaseRxJava.<BaseBean<UserInfoBean>>io_main_loading(mPresenter))
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresenter) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
    }
}


