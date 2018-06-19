package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class ReadCardsPresenter(view: ReadCardsContract.View) : BasePresenter<ReadCardsContract.View>(), ReadCardsContract.Presenter {

    private val mModel: ReadCardsModel

    init {
        attachView(view)
        mModel = ReadCardsModel(this)
    }

    fun getEmptyRecommendCard() {
        mModel.getEmptyRecommendCard()
    }

    fun getReadCard(page: Int, pageSize: Int) {
        mModel.getReadCard(page, pageSize)
    }

    fun deleteReadCard(realId: Int,cardId: Int) {
        mModel.deleteReadCard(realId,cardId)
    }

    override fun getEmptyRecommendCardSuccess(bean: CardBean) {
        mView.getEmptyRecommendCardSuccess(bean)
    }

    override fun getDataSuccess(list: List<ReadCardsBean>) {
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

