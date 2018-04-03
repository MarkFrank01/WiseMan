package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean

class SubjectPresenter(view: SubjectContract.View) : BasePresenter<SubjectContract.View>(), SubjectContract.Presenter {

    private val mModel: SubjectModel

    init {
        attachView(view)
        mModel = SubjectModel(this)
    }

    fun getSubject(id: Int, page: Int, pageSize: Int) {
        mModel.getSubject(id, page, pageSize)
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

