package com.zxcx.shitang.ui.home.hot;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface HotContract {

    interface View extends MvpView<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }

    interface Presenter extends IBasePresenter<List<HotCardBean>> {
        void getHotCardBagSuccess(List<HotCardBagBean> list);
    }
}

