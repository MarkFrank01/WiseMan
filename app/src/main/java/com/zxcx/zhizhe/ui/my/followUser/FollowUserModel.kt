package com.zxcx.zhizhe.ui.my.followUser

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber

class FollowUserModel(presenter: FollowUserContract.Presenter) : BaseModel<FollowUserContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getEmptyFollowUser() {
        mDisposable = AppClient.getAPIService().emptyFollowUser
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<FollowUserBean>>(mPresenter) {
                    override fun onNext(list: MutableList<FollowUserBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getFollowUser(followType :Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getFollowUser(followType,0, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<FollowUserBean>>(mPresenter) {
                    override fun onNext(list: MutableList<FollowUserBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun followUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId, 0)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<FollowUserBean>(mPresenter) {
                    override fun onNext(bean: FollowUserBean) {
                        mPresenter?.postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun unFollowUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId, 1)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<FollowUserBean>(mPresenter) {
                    override fun onNext(bean: FollowUserBean) {
                        mPresenter?.unFollowUserSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }
}


