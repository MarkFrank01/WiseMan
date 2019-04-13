package com.zxcx.zhizhe.ui.my.selectAttention.man

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectManModel(presenter:NowSelectManContract.Presenter):BaseModel<NowSelectManContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getInterestRecommendForUser(){
        mDisposable =AppClient.getAPIService().interestRecommendForUser
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<SearchUserBean>>(mPresenter){
                    override fun onNext(t: MutableList<SearchUserBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //关注用户
    fun followUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,0)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<SearchUserBean>(mPresenter){
                    override fun onNext(t: SearchUserBean) {
                        mPresenter?.mFollowUserSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //取消关注用户
    fun unFollowUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,1)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<SearchUserBean>(mPresenter){
                    override fun onNext(t: SearchUserBean) {
                        mPresenter?.unFollowUserSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}