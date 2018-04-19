package com.zxcx.zhizhe.ui.search.result

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SearchResultPresenter(view: SearchResultContract.View) : BasePresenter<SearchResultContract.View>(), SearchResultContract.Presenter {

    private val mModel: SearchResultModel

    init {
        attachView(view)
        mModel = SearchResultModel(this)
    }

    fun search(keyword: String, page: Int, pageSize: Int) {
        mModel.search(keyword, page, pageSize)
    }

    override fun getDataSuccess(bean: MutableList<SearchResultBean>) {
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

