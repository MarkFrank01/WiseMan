package com.zxcx.shitang.ui.loginAndRegister.selectAttention;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface SelectAttentionContract {

    interface View extends MvpView<SelectAttentionBean> {
        void postSuccess();
        void postFail();
    }

    interface Presenter extends IBasePresenter<SelectAttentionBean> {
        void selectAttention(List<String> cardBagId);
    }
}

