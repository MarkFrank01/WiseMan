package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SystemMessagePresenter(view: SystemMessageContract.View) : BasePresenter<SystemMessageContract.View>(), SystemMessageContract.Presenter {

    private val mModel: SystemMessageModel

    init {
        attachView(view)
        mModel = SystemMessageModel(this)
    }

    fun getSystemMessage(page: Int, pageSize: Int) {
        mModel.getSystemMessage(page, pageSize)
    }

    override fun getDataSuccess(list: List<SystemMessageBean>) {
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

