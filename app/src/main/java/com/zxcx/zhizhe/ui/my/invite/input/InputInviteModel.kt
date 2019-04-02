package com.zxcx.zhizhe.ui.my.invite.input

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.invite.InviteBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class InputInviteModel(presenter:InputInviteContract.Presenter):BaseModel<InputInviteContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun inputInvitationCode(code:String){
        mDisposable = AppClient.getAPIService().inputInvitationCode(code)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<InviteBean>(mPresenter){
                    override fun onNext(t: InviteBean) {
                        mPresenter?.inputInvitationCodeSuccess(t)
                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.errormsg("")
                    }
                })
        addSubscription(mDisposable)
    }

}