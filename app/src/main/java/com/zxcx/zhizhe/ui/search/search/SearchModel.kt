package com.zxcx.zhizhe.ui.search.search

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.room.AppDatabase
import com.zxcx.zhizhe.room.SearchHistory
import com.zxcx.zhizhe.utils.LogCat
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.util.concurrent.TimeUnit

class SearchModel(present: SearchContract.Presenter) : BaseModel<SearchContract.Presenter>() {
    init {
        this.mPresenter = present
    }

    fun getSearchBean() {
        mDisposable = Flowable.zip(
                AppClient.getAPIService().searchHot.compose<MutableList<HotSearchBean>>(BaseRxJava.handleArrayResult()),
                AppDatabase.getInstance().mSearchHistoryDao().flowableAll,
                BiFunction<MutableList<HotSearchBean>,MutableList<SearchHistory>,SearchBean> { t1, t2 ->
                    val hotSearchList:MutableList<String> = mutableListOf()
                    val searchHistoryList:MutableList<String> = mutableListOf()
                    t1.forEach {
                        hotSearchList.add(it.conent)
                    }
                    (if (t2.size > 20) t2.subList(0,20) else t2).forEach {
                        searchHistoryList.add(it.keyword)
                    }
                    SearchBean(hotSearchList,searchHistoryList)
                })
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<SearchBean>(mPresenter) {
                    override fun onNext(bean: SearchBean) {
                        mPresenter?.getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getSearchPre(keyword: String) {
        mDisposable = Flowable.just(keyword)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter { s -> s.isNotEmpty() }
                .switchMap {
                    AppClient.getAPIService().getSearchPre(keyword)
                            .compose<MutableList<String>>(BaseRxJava.handleArrayResult())
                }
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MutableList<String>>(mPresenter) {
                    override fun onNext(list: MutableList<String>) {
                        mPresenter?.getSearchPreSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun deleteAllSearchHistory(){
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
}


