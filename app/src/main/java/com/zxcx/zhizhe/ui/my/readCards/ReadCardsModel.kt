package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

class ReadCardsModel(presenter: ReadCardsContract.Presenter) : BaseModel<ReadCardsContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getEmptyRecommendCard(tabType: Int) {
		mDisposable = AppClient.getAPIService().getEmptyRecommendCard(tabType)
				.compose(BaseRxJava.io_main())
				.compose<CardBean>(BaseRxJava.handleResult())
				.subscribeWith(object : BaseSubscriber<CardBean>(mPresenter) {
					override fun onNext(bean: CardBean) {
						mPresenter?.getEmptyRecommendCardSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	fun getReadCard(sortType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getReadCard(sortType, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
					override fun onNext(list: List<CardBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun deleteReadCard(realId: Int, cardId: Int) {
		mDisposable = AppClient.getAPIService().removeReadArticle(realId, cardId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(t: BaseBean<*>?) {
						mPresenter?.postSuccess()
					}
				})
		addSubscription(mDisposable)
	}

	fun getCollectCard(sortType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getCollectCard(sortType, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose<List<CardBean>>(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
					override fun onNext(list: List<CardBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun deleteCollectCard(cardId: Int) {
		mDisposable = AppClient.getAPIService().removeCollectArticle(cardId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(t: BaseBean<*>?) {
						mPresenter?.postSuccess()
					}
				})
		addSubscription(mDisposable)
	}

	fun getLikeCard(sortType: Int, page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getLikeCard(sortType, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
					override fun onNext(list: List<CardBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun deleteLikeCard(cardId: Int) {
		mDisposable = AppClient.getAPIService().removeLikeArticle(cardId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(t: BaseBean<*>?) {
						mPresenter?.postSuccess()
					}
				})
		addSubscription(mDisposable)
	}
}


