package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseArrayBean
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class RankModel(presenter: RankContract.Presenter) : BaseModel<RankContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getMyRank(){
        mDisposable = Flowable.zip(AppClient.getAPIService().myRank,AppClient.getAPIService().topTenRank,
                BiFunction<BaseBean<UserRankBean>, BaseArrayBean<UserRankBean>, RankBean> { t1, t2 ->
                    RankBean(t1.data,t2.data)
                })
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<RankBean>(mPresenter) {
                    override fun onNext(bean: RankBean) {
                        mPresenter.getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }
}


