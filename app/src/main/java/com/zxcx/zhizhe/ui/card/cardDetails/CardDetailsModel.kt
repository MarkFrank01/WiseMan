package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.BaseModel

class CardDetailsModel(presenter: CardDetailsContract.Presenter) : BaseModel<CardDetailsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }
}


