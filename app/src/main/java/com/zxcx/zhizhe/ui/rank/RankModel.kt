package com.zxcx.zhizhe.ui.rank

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

class RankModel(presenter: RankContract.Presenter) : BaseModel<RankContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getAD() {
		mDisposable = AppClient.getAPIService().getAD("400")
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter) {
					override fun onNext(list: MutableList<ADBean>) {
						mPresenter?.getADSuccess(list)
					}
				})
	}

    fun getDialogAD(lastOpenedTime: Long, lastOpenedAdId: Long){
        mDisposable = AppClient.getAPIService().getAD("103",lastOpenedTime,lastOpenedAdId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter) {
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getDialogADSuccess(list)
                    }
                })
    }

	fun getMyRank() {
		mDisposable = AppClient.getAPIService().myRank
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleResult())
				.subscribeWith(object : BaseSubscriber<UserRankBean>(mPresenter) {
					override fun onNext(bean: UserRankBean) {
						mPresenter?.getMyRankSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	fun getTopTenRank() {
		mDisposable = AppClient.getAPIService().topTenRank
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<UserRankBean>>(mPresenter) {
					override fun onNext(list: List<UserRankBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}
}


