package com.zxcx.zhizhe.ui.card.cardDetails

import android.os.Bundle

import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity

class CardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)

    }

    override fun createPresenter(): CardDetailsPresenter {
        return CardDetailsPresenter(this)
    }

    override fun getDataSuccess(bean: CardDetailsBean) {

    }
}
