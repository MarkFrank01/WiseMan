package com.zxcx.zhizhe.ui.card.card.cardDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;

public interface CardDetailsContract {

    interface View extends GetPostView<CardDetailsBean,CardDetailsBean> {
        void followSuccess();
        void likeSuccess(CardDetailsBean bean);
        void collectSuccess(CardDetailsBean bean);
    }

    interface Presenter extends IGetPostPresenter<CardDetailsBean,CardDetailsBean> {
        void followSuccess();
        void likeSuccess(CardDetailsBean bean);
        void collectSuccess(CardDetailsBean bean);
    }
}

