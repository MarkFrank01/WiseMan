package com.zxcx.zhizhe.ui.my.creation.passed

import com.zxcx.zhizhe.mvpBase.BasePresenter

class CreationPresenter(view: CreationContract.View) : BasePresenter<CreationContract.View>(), CreationContract.Presenter {

    private val mModel: CreationModel

    init {
        attachView(view)
        mModel = CreationModel(this)
    }

    fun getCreation(passType: Int, page: Int, pageSize: Int) {
        mModel.getCreation(passType, page, pageSize)
    }

    override fun getDataSuccess(bean: List<CreationBean>) {
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

