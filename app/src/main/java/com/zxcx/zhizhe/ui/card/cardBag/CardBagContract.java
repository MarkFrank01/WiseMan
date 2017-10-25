package com.zxcx.zhizhe.ui.card.cardBag;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.GetView;

import java.util.List;

public interface CardBagContract {

    interface View extends GetView<List<CardBagBean>> {

    }

    interface Presenter extends IGetPresenter<List<CardBagBean>> {

    }
}

