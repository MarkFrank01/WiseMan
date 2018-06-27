package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class CardDetailsPresenter(view: CardDetailsContract.View) : BasePresenter<CardDetailsContract.View>(), CardDetailsContract.Presenter {

    private val mModel: CardDetailsModel

    init {
        attachView(view)
        mModel = CardDetailsModel(this)
    }

    fun readCard(cardId: Int) {
        mModel.readCard(cardId)
    }

    fun likeCard(cardId: Int) {
        mModel.likeCard(cardId)
    }

    fun removeLikeCard(cardId: Int) {
        mModel.removeLikeCard(cardId)
    }

    fun unLikeCard(cardId: Int) {
        mModel.unLikeCard(cardId)
    }

    fun removeUnLikeCard(cardId: Int) {
        mModel.removeUnLikeCard(cardId)
    }

    fun addCollectCard(cardId: Int) {
        mModel.addCollectCard(cardId)
    }

    fun removeCollectCard(cardId: Int) {
        mModel.removeCollectCard(cardId)
    }

    fun setUserFollow(authorId: Int, followType: Int, bean: CardBean) {
        mModel.setUserFollow(authorId, followType, bean)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        mView.getDataSuccess(list)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun followSuccess(bean: CardBean) {
        mView.followSuccess(bean)
    }

    override fun likeSuccess(bean: CardBean) {
        mView.likeSuccess(bean)
    }

    override fun collectSuccess(bean: CardBean) {
        mView.collectSuccess(bean)
    }

    override fun postSuccess(bean: CardBean?) {
        mView.postSuccess(bean)
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

