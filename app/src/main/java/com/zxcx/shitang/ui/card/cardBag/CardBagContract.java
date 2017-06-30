package com.zxcx.shitang.ui.card.cardBag;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.ui.card.cardBag.CardBagBean;

public interface CardBagContract {

    interface View extends MvpView<CardBagBean> {

    }

    interface Presenter extends IBasePresenter<CardBagBean> {

    }
}

