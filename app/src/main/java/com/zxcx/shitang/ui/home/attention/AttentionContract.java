package com.zxcx.shitang.ui.home.attention;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;

import java.util.List;

public interface AttentionContract {

    interface View extends MvpView<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }

    interface Presenter extends IBasePresenter<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }
}

