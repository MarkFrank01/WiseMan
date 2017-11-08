package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;

public interface CardDetailsContract {

    interface View extends GetPostView<CardDetailsBean,CardDetailsBean> {
        void UnCollectSuccess();
    }

    interface Presenter extends IGetPostPresenter<CardDetailsBean,CardDetailsBean> {
        void UnCollectSuccess();
    }
}

