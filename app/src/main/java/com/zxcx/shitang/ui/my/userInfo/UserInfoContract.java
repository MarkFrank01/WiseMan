package com.zxcx.shitang.ui.my.userInfo;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IGetPresenter;

public interface UserInfoContract {

    interface View extends MvpView<UserInfoBean> {

    }

    interface Presenter extends IGetPresenter<UserInfoBean> {

    }
}

