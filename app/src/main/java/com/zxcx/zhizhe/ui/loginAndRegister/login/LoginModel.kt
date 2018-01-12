package com.zxcx.zhizhe.ui.loginAndRegister.login

import com.zxcx.zhizhe.App
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat

class LoginModel(present: LoginContract.Presenter) : BaseModel<LoginContract.Presenter>() {

    init {
        this.mPresenter = present
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

    fun channelLogin(channelType: Int?, openId:String?, jpushRID:String?, appType: Int?, appChannel:String?, appVersion:String?) {
        mDisposable = AppClient.getAPIService().channelLogin(channelType!!, openId, jpushRID, appType!!, appChannel, appVersion)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object : BaseSubscriber<LoginBean>(mPresenter) {
                    override fun onNext(loginBean: LoginBean) {
                        mPresenter?.channelLoginSuccess(loginBean)
                    }

                    override fun onError(t: Throwable) {
                        if (t.message != null) {
                            val code = t.message?.substring(0, 3)
                            try {
                                val code1 = Integer.parseInt(code)
                                val message = t.message?.substring(3)
                                t.printStackTrace()
                                LogCat.d(t.message)
                                if (Constants.NEED_LOGIN == code1) {
                                    mPresenter?.channelLoginNeedRegister()
                                } else {
                                    mPresenter?.getDataFail(message)
                                }
                            } catch (e: NumberFormatException) {
                                mPresenter?.getDataFail(App.getContext().getString(R.string.network_error))
                            }

                        } else {
                            mPresenter?.getDataFail(App.getContext().getString(R.string.network_error))
                        }
                    }
                })
        addSubscription(mDisposable)
    }
}


