package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SearchCardPresenter(view: SearchCardContract.View) : BasePresenter<SearchCardContract.View>(), SearchCardContract.Presenter {

    private val mModel: SearchCardModel

    init {
        attachView(view)
        mModel = SearchCardModel(this)
    }

    fun searchCard(keyword: String, page: Int, pageSize: Int) {
        mModel.searchCard(keyword, page, pageSize)
    }

    override fun getDataSuccess(bean: List<SearchCardBean>) {
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

