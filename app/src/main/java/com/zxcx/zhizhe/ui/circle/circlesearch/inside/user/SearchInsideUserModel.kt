package com.zxcx.zhizhe.ui.circle.circlesearch.inside.user

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideUserModel(presenter:SearchInsideUserContract.Presenter):BaseModel<SearchInsideUserContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun searchUser(page:Int,pageSIze:Int,circleId:Int,keyword:String){
        mDisposable = AppClient.getAPIService().searchCircleUsers(page,pageSIze,circleId, keyword)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<SearchUserBean>>(mPresenter){
                    override fun onNext(t: MutableList<SearchUserBean>) {
                        mPresenter?.getDataSuccess(t)
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