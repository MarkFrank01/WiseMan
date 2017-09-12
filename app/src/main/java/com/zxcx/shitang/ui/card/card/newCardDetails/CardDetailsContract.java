package com.zxcx.shitang.ui.card.card.newCardDetails;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;

public interface CardDetailsContract {

    interface View extends GetPostView<CardDetailsBean,PostBean> {

    }

    interface Presenter extends IGetPostPresenter<CardDetailsBean,PostBean> {

    }
}

