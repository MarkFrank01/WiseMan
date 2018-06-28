package com.zxcx.zhizhe.ui.my.creation.fragment

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class CreationModel(presenter: CreationContract.Presenter) : BaseModel<CreationContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getCreation(passType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getCreation(passType, 0, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<CreationBean>>(mPresenter) {
					override fun onNext(list: List<CreationBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}
}


