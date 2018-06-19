package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsContract
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsModel
import com.zxcx.zhizhe.mvpBase.BasePresenter

class CardDetailsPresenter(view: CardDetailsContract.View) : BasePresenter<CardDetailsContract.View>(), CardDetailsContract.Presenter {

    private val mModel: CardDetailsModel

    init {
        attachView(view)
        mModel = CardDetailsModel(this)
    }

    override fun getDataSuccess(bean: CardDetailsBean) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun startLogin() {
        mView.startLogin()
    }

    override fun detachView() {
        super.detachView()
        mModel.onDestroy()
    }
}

