package com.zxcx.zhizhe.ui.search.search

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.room.AppDatabase
import com.zxcx.zhizhe.utils.LogCat
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class SearchModel(present: SearchContract.Presenter) : BaseModel<SearchContract.Presenter>() {
	init {
		this.mPresenter = present
	}

	fun getSearchBean() {
		mDisposable = AppClient.getAPIService().searchHot
				.compose(BaseRxJava.handleArrayResult())
				.map {
					val hotSearchList: MutableList<String> = mutableListOf()
					it.forEach {
						hotSearchList.add(it.conent)
					}
					hotSearchList
				}
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<String>>(mPresenter) {
					override fun onNext(bean: MutableList<String>) {
						mPresenter?.getDataSuccess(bean)
					}
				})
		addSubscription(mDisposable)
		mDisposable = AppDatabase.getInstance().mSearchHistoryDao().flowableAll
				.map {
					val searchHistoryList: MutableList<String> = mutableListOf()
					(if (it.size > 20) it.subList(0, 20) else it).forEach {
						searchHistoryList.add(it.keyword)
					}
					searchHistoryList
				}
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<String>>(mPresenter) {
					override fun onNext(bean: MutableList<String>) {
						mPresenter?.getSearchHistorySuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	fun getSearchPre(keyword: String) {
		mDisposable = AppClient.getAPIService().getSearchPre(keyword)
				.compose<MutableList<String>>(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<String>>(mPresenter) {
					override fun onNext(list: MutableList<String>) {
						mPresenter?.getSearchPreSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun deleteAllSearchHistory() {
		mDisposable = Flowable.just(0)
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(Schedulers.io())
				.map { AppDatabase.getInstance().mSearchHistoryDao().deleteAll() }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(object : DisposableSubscriber<Int>() {
					override fun onNext(aVoid: Int?) {
						mPresenter?.deleteHistorySuccess()
					}

					override fun onError(t: Throwable) {
						LogCat.e("删除失败", t)
					}

					override fun onComplete() {}
				})

		addSubscription(mDisposable)
	}

	fun getSearchDefaultKeyword() {
		mDisposable = AppClient.getAPIService().searchDefaultKeyword
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<HotSearchBean>(mPresenter) {
					override fun onNext(t: HotSearchBean) {
						mPresenter?.getSearchDefaultKeywordSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}
}


