package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;

public interface CardDetailsContract {

    interface View extends NullGetPostView<CardDetailsBean> {
        void UnCollectSuccess();
    }

    interface Presenter extends INullGetPostPresenter<CardDetailsBean> {
        void UnCollectSuccess();
    }
}

