package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.utils.Constants

class HotCardModel(present: HotCardContract.Presenter) : BaseModel<HotCardContract.Presenter>() {

	init {
		this.mPresenter = present
	}

	fun getHotCard(lastRefresh: String, page: Int) {
//		mDisposable = AppClient.getAPIService().getHotCard(lastRefresh, page, Constants.PAGE_SIZE)
		mDisposable = AppClient.getAPIService().getHotCardNew(lastRefresh, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter) {
					override fun onNext(list: MutableList<CardBean>) {
						mPresenter!!.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

    fun getAD(){
        mDisposable = AppClient.getAPIService().getAD("401")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    //邀请码
    fun inputInvitationCode(code:String){
        mDisposable = AppClient.getAPIService().inputInvitationCode(code)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<InviteBean>(mPresenter){
                    override fun onNext(t: InviteBean) {
                        mPresenter?.inputInvitationCodeSuccess(t)
                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.errormsg(t.message.toString())
                    }
                })
        addSubscription(mDisposable)
    }
}


