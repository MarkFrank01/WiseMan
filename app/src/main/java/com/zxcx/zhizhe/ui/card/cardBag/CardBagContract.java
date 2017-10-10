package com.zxcx.zhizhe.ui.card.cardBag;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.MvpView;

import java.util.List;

public interface CardBagContract {

    interface View extends MvpView<List<CardBagBean>> {

    }

    interface Presenter extends IGetPresenter<List<CardBagBean>> {

    }
}

