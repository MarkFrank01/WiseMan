package com.zxcx.zhizhe.ui.loginAndRegister.passwordLogin

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean

class PasswordLoginModel(presenter: PasswordLoginContract.Presenter) : BaseModel<PasswordLoginContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun phoneLogin(phone:String?, password:String?, jpushRID:String?, appType: Int?, appChannel:String?, appVersion:String?) {
        mDisposable = AppClient.getAPIService().phoneLogin(phone, password, jpushRID, appType!!, appChannel, appVersion)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object : BaseSubscriber<LoginBean>(mPresenter) {
                    override fun onNext(loginBean: LoginBean) {
                        mPresenter?.getDataSuccess(loginBean)
                    }
                })
        addSubscription(mDisposable)
    }
}


