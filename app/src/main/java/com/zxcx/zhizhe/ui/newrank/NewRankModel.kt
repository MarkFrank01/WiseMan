package com.zxcx.zhizhe.ui.newrank

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankModel(presenter:NewRankContract.Presenter):BaseModel<NewRankContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getMyRank(){
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
                    override fun onNext(t: List<UserRankBean>) {
                        mPresenter?.getTopTenRankSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子顶部的广告
    fun getAD(){
        mDisposable = AppClient.getAPIService().getAD("400")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}