package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.utils.Constants

class HotCardModel(present: HotCardContract.Presenter) : BaseModel<HotCardContract.Presenter>() {

	init {
		this.mPresenter = present
	}

	fun getHotCard(lastRefresh: String, page: Int) {
		mDisposable = AppClient.getAPIService().getHotCard(lastRefresh, page, Constants.PAGE_SIZE)
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
        mDisposable = AppClient.getAPIService().getAD("400")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }
                })

    }
}


