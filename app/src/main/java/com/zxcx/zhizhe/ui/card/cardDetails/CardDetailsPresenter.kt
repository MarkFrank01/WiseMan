package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class CardDetailsPresenter(view: CardDetailsContract.View) : BasePresenter<CardDetailsContract.View>(), CardDetailsContract.Presenter {

    private val mModel: CardDetailsModel

    init {
        attachView(view)
        mModel = CardDetailsModel(this)
    }

    fun getCardDetails(cardId: Int) {
        mModel.getCardDetails(cardId)
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

    fun addCollectCard(cardId: Int) {
        mModel.addCollectCard(cardId)
    }

    fun removeCollectCard(cardId: Int) {
        mModel.removeCollectCard(cardId)
    }

    fun deleteNote(noteId: Int) {
        mModel.deleteNote(noteId)
    }

    fun setUserFollow(authorId: Int, followType: Int, bean: CardBean) {
        mModel.setUserFollow(authorId, followType, bean)
    }

    fun saveCardNode(title: String?, imageUrl: String?, withCardId: Int, content: String?,bean1: CardBean) {
        mModel.saveCardNode(title,imageUrl,withCardId,content,bean1)
    }

    fun saveCardNode( withCardId: Int,styleType:Int,cardBean:CardBean){
        mModel.saveCardNode(withCardId,styleType,cardBean)
    }

    override fun getDataSuccess(bean: CardBean) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun followSuccess(bean: CardBean) {
        mView.followSuccess(bean)
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

    override fun deleteSuccess() {
    }
}

