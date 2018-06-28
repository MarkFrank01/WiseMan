package com.zxcx.zhizhe.ui.home.rank.moreRank

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.home.rank.UserRankBean

class RankModel(presenter: RankContract.Presenter) : BaseModel<RankContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getTopHundredRank(page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getTopHundredRank(page, pageSize)
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


