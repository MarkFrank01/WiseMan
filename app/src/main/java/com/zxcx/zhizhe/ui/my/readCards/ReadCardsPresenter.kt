package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.BasePresenter

class ReadCardsPresenter(view: ReadCardsContract.View) : BasePresenter<ReadCardsContract.View>(), ReadCardsContract.Presenter {

    private val mModel: ReadCardsModel

    init {
        attachView(view)
        mModel = ReadCardsModel(this)
    }

    fun getReadCard(sortType: Int, page: Int, pageSize: Int) {
        mModel.getReadCard(sortType, page, pageSize)
    }

    override fun getDataSuccess(list: List<ReadCardsBean>) {
        mView.getDataSuccess(list)
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

