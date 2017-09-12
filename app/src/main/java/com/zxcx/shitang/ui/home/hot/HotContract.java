package com.zxcx.shitang.ui.home.hot;

import com.zxcx.shitang.mvpBase.IGetPresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface HotContract {

    interface View extends MvpView<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }

    interface Presenter extends IGetPresenter<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }
}

