package com.zxcx.zhizhe.ui.search.result.subject

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SearchSubjectPresenter(view: SearchSubjectContract.View) : BasePresenter<SearchSubjectContract.View>(), SearchSubjectContract.Presenter {

    private val mModel: SearchSubjectModel

    init {
        attachView(view)
        mModel = SearchSubjectModel(this)
    }

    fun searchSubject(keyword: String, page: Int, pageSize: Int) {
        mModel.searchSubject(keyword, page, pageSize)
    }

    override fun getDataSuccess(bean: List<SubjectBean>) {
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

