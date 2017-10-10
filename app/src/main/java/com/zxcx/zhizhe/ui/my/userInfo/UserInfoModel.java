package com.zxcx.zhizhe.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.PostSubscriber;

public class UserInfoModel extends BaseModel<UserInfoContract.Presenter> {
    public UserInfoModel(@NonNull UserInfoContract.Presenter present) {
        this.mPresent = present;
    }

    public void getOSS(String uuid){
        mDisposable = AppClient.getAPIService().getOSS(uuid)
                .compose(this.<BaseBean<OSSTokenBean>>io_main())
                .compose(this.<OSSTokenBean>handleResult())
                .subscribeWith(new BaseSubscriber<OSSTokenBean>(mPresent) {
                    @Override
                    public void onNext(OSSTokenBean bean) {
                        mPresent.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void changeImageUrl(String imageUrl){
        mDisposable = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null)
                .compose(this.<BaseBean<UserInfoBean>>io_main())
                .compose(this.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresent) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
    }

    public void changeBirth(String birth){
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, birth)
                .compose(this.<BaseBean<UserInfoBean>>io_main())
                .compose(this.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresent) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
    }
}


