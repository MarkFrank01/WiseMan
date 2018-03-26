package com.zxcx.zhizhe.ui.my.collect

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean

class CollectCardPresenter(view: CollectCardContract.View) : BasePresenter<CollectCardContract.View>(), CollectCardContract.Presenter {

    private val mModel: CollectCardModel

    init {
        attachView(view)
        mModel = CollectCardModel(this)
    }

    fun getCollectCard(page: Int, pageSize: Int) {
        mModel.getCollectCard(page, pageSize)
    }

    fun deleteCollectCard(cardId: Int) {
        mModel.deleteCollectCard(cardId)
    }

    override fun getDataSuccess(bean: List<MyCardsBean>) {
        mView.getDataSuccess(bean)
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

