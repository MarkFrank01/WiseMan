package com.zxcx.zhizhe.ui.loginAndRegister.passwordLogin

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean

class PasswordLoginPresenter(view: PasswordLoginContract.View) : BasePresenter<PasswordLoginContract.View>(), PasswordLoginContract.Presenter {

    private val mModel: PasswordLoginModel

    init {
        attachView(view)
        mModel = PasswordLoginModel(this)
    }

    fun phoneLogin(phone: String, password: String, jpushRID: String, appType: Int, appChannel: String, appVersion: String) {
        mModel.phoneLogin(phone, password, jpushRID, appType, appChannel, appVersion)
    }

    override fun getDataSuccess(bean: LoginBean) {
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

