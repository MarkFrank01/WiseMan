package com.zxcx.shitang.ui.card.cardBag;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface CardBagContract {

    interface View extends MvpView<List<CardBagBean>> {

    }

    interface Presenter extends IBasePresenter<List<CardBagBean>> {

    }
}

