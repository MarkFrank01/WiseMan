package com.zxcx.shitang.ui.my.userInfo;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IGetPresenter;

public interface UserInfoContract {

    interface View extends MvpView<OSSTokenBean> {

    }

    interface Presenter extends IGetPresenter<OSSTokenBean> {

    }
}

