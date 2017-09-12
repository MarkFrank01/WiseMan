package com.zxcx.shitang.ui.home.attention;

import com.zxcx.shitang.mvpBase.IGetPresenter;
import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;

import java.util.List;

public interface AttentionContract {

    interface View extends MvpView<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }

    interface Presenter extends IGetPresenter<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }
}

