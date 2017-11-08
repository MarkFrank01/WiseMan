package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public interface CardBagCardDetailsContract {

    interface View extends NullGetPostView<CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
        void likeSuccess();
        void unLikeSuccess();
        void UnCollectSuccess();
    }

    interface Presenter extends INullGetPostPresenter<CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
        void likeSuccess();
        void unLikeSuccess();
        void UnCollectSuccess();
    }
}

