package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public interface CardBagCardDetailsContract {

    interface View extends GetPostView<CardDetailsBean,CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
        void UnCollectSuccess();
    }

    interface Presenter extends IGetPostPresenter<CardDetailsBean,CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
        void UnCollectSuccess();
    }
}

