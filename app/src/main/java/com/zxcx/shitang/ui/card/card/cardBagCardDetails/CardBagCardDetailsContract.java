package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import com.zxcx.shitang.mvpBase.INullGetPostPresenter;
import com.zxcx.shitang.mvpBase.NullGetPostView;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsBean;

import java.util.List;

public interface CardBagCardDetailsContract {

    interface View extends NullGetPostView<CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
    }

    interface Presenter extends INullGetPostPresenter<CardDetailsBean> {
        void getAllCardIdSuccess(List<CardBagCardDetailsBean> list);
    }
}

