package com.zxcx.zhizhe.ui.my.invite

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class MyInviteModel(presenter: MyInviteContract.Presenter) : BaseModel<MyInviteContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getInvitationHistory() {
        mDisposable = AppClient.getAPIService().invitationHistory
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<InviteBean>>(mPresenter) {
                    override fun onNext(t: MutableList<InviteBean>) {
                        mPresenter?.getInvitationHistorySuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getInvitationInfo() {
        mDisposable = AppClient.getAPIService().invitationInfo
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<InviteBean>(mPresenter){
                    override fun onNext(t: InviteBean) {
                        mPresenter?.getInvitationInfoSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}