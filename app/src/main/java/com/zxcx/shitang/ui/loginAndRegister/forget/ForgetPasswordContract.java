package com.zxcx.shitang.ui.loginAndRegister.forget;

import com.zxcx.shitang.mvpBase.IPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.mvpBase.PostView;

public interface ForgetPasswordContract {

    interface View extends PostView<PostBean> {

    }

    interface Presenter extends IPostPresenter<PostBean> {

    }
}

