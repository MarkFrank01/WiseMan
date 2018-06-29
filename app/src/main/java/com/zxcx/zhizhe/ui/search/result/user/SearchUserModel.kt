package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.Constants

class SearchUserModel(presenter: SearchUserContract.Presenter) : BaseModel<SearchUserContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun searchUser(keyword: String, page: Int) {
		mDisposable = AppClient.getAPIService().searchUser(keyword, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<SearchUserBean>>(mPresenter) {
					override fun onNext(list: List<SearchUserBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun followUser(authorId: Int) {
		mDisposable = AppClient.getAPIService().setUserFollow(authorId, 0)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : PostSubscriber<SearchUserBean>(mPresenter) {
					override fun onNext(bean: SearchUserBean) {
						mPresenter?.postSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	fun unFollowUser(authorId: Int) {
		mDisposable = AppClient.getAPIService().setUserFollow(authorId, 1)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : PostSubscriber<SearchUserBean>(mPresenter) {
					override fun onNext(bean: SearchUserBean) {
						mPresenter?.unFollowUserSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}
}