package com.zxcx.zhizhe.ui.my.creation.fragment

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

class CreationModel(presenter: CreationContract.Presenter) : BaseModel<CreationContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getCreation(passType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getCreation(passType, 0, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
					override fun onNext(list: List<CardBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}
}


