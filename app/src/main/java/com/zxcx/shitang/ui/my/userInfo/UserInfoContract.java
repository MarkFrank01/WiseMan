package com.zxcx.shitang.ui.my.userInfo;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;

public interface UserInfoContract {

    interface View extends GetPostView<OSSTokenBean,UserInfoBean> {

    }

    interface Presenter extends IGetPostPresenter<OSSTokenBean,UserInfoBean> {

    }
}

