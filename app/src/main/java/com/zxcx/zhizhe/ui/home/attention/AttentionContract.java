package com.zxcx.zhizhe.ui.home.attention;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

public interface AttentionContract {

    interface View extends GetView<List<CardBean>> {
    }

    interface Presenter extends IGetPresenter<List<CardBean>> {
    }
}

