package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsBean

interface CardDetailsContract {

    interface View : GetView<CardDetailsBean>

    interface Presenter : IGetPresenter<CardDetailsBean>
}

