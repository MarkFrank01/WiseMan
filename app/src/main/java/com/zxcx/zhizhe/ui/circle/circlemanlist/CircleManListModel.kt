package com.zxcx.zhizhe.ui.circle.circlemanlist

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleManListModel(presenter:CircleManListContract.Presnter):BaseModel<CircleManListContract.Presnter>(){

    init {
        this.mPresenter = presenter
    }

    //获取圈子成员
    fun getCircleMemberByCircleId(orderType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleMemberByCircleId(orderType, circleId, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getCircleMemberByCircleIdSuccess(t)
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