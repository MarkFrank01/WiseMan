package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.App
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.creation.creationDetails.RejectDetailsBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat

class SystemMessageModel(presenter: SystemMessageContract.Presenter) : BaseModel<SystemMessageContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getSystemMessage(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getSystemMessage(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<SystemMessageBean>>(mPresenter) {
                    override fun onNext(list: List<SystemMessageBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getRejectDetails(cardId: Int) {
        mDisposable = AppClient.getAPIService().getRejectDetails(cardId,2)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<RejectDetailsBean>(mPresenter) {
                    override fun onNext(bean: RejectDetailsBean) {
                        mPresenter.getCardSuccess(bean.id)
                    }

                    override fun onError(t: Throwable?) {
                        if (t?.message != null) {
                            val code = t.message?.substring(0, 3)
                            try {
                                val code1 = Integer.parseInt(code)
                                val message = t.message?.substring(3)
                                t.printStackTrace()
                                LogCat.d(t.message)
                                if (Constants.NEED_LOGIN == code1) {
                                    mPresenter.getCardNoFound()
                                } else {
                                    mPresenter.getDataFail(message)
                                }
                            } catch (e: NumberFormatException) {
                                mPresenter.getDataFail(App.getContext().getString(R.string.network_error))
                            }

                        } else {
                            mPresenter.getDataFail(App.getContext().getString(R.string.network_error))
                        }
                    }
                })
        addSubscription(mDisposable)
    }
}


