package com.zxcx.zhizhe.ui.card.card.cardDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;

public interface CardDetailsContract {

    interface View extends GetPostView<CardDetailsBean,CardDetailsBean> {

    }

    interface Presenter extends IGetPostPresenter<CardDetailsBean,CardDetailsBean> {

    }
}

