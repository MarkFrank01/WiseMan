package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.home.hot.CardBean

class LikeCardsPresenter(view: LikeCardsContract.View) : BasePresenter<LikeCardsContract.View>(), LikeCardsContract.Presenter {

    private val mModel: LikeCardsModel

    init {
        attachView(view)
        mModel = LikeCardsModel(this)
    }

    fun getEmptyRecommendCard() {
        mModel.getEmptyRecommendCard()
    }

    fun getLikeCard(page: Int, pageSize: Int) {
        mModel.getLikeCard(page, pageSize)
    }

    fun deleteLikeCard(cardId: Int) {
        mModel.deleteLikeCard(cardId)
    }

    override fun getEmptyRecommendCardSuccess(bean: CardBean) {
        mView.getEmptyRecommendCardSuccess(bean)
    }

    override fun getDataSuccess(list: List<MyCardsBean>) {
        mView.getDataSuccess(list)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun postSuccess() {
        mView.postSuccess()
    }

    override fun postFail(msg: String?) {
        mView.postFail(msg)
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

