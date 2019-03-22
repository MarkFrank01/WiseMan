package com.zxcx.zhizhe.ui.newrank.morerank

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankModel(presenter:MoreRankContract.Presenter):BaseModel<MoreRankContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    //获取更多排名
    fun getMoreRank(page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getTopHundredRank(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<List<UserRankBean>>(mPresenter){
                    override fun onNext(t: List<UserRankBean>) {
                        mPresenter?.getMoreRankSuccess(t)
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
                        mPresenter?.followUserSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

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