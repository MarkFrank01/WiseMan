package com.zxcx.zhizhe.ui.otherUser

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

class OtherUserModel(presenter: OtherUserContract.Presenter) : BaseModel<OtherUserContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getOtherUserInfo(id: Int) {
		mDisposable = AppClient.getAPIService().getAuthorInfo(id)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<OtherUserInfoBean>(mPresenter) {
					override fun onNext(t: OtherUserInfoBean) {
						mPresenter?.getDataSuccess(t)
					}
				})
	}

	fun getOtherUserCreation(id: Int, sortType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getOtherUserCreation(id, sortType, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter) {
					override fun onNext(list: MutableList<CardBean>) {
						mPresenter?.getOtherUserCreationSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}
}


