package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SearchUserPresenter(view: SearchUserContract.View) : BasePresenter<SearchUserContract.View>(), SearchUserContract.Presenter {

    private val mModel: SearchUserModel

    init {
        attachView(view)
        mModel = SearchUserModel(this)
    }

    fun searchUser(keyword: String, page: Int, pageSize: Int) {
        mModel.searchUser(keyword, page, pageSize)
    }

    override fun getDataSuccess(bean: List<SearchUserBean>) {
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

