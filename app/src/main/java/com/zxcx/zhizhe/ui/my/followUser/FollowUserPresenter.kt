package com.zxcx.zhizhe.ui.my.followUser

import com.zxcx.zhizhe.mvpBase.BasePresenter

class FollowUserPresenter(view: FollowUserContract.View) : BasePresenter<FollowUserContract.View>(), FollowUserContract.Presenter {

    private val mModel: FollowUserModel

    init {
        attachView(view)
        mModel = FollowUserModel(this)
    }

    fun getEmptyFollowUser() {
        mModel.getEmptyFollowUser()
    }

    fun getFollowUser(followType :Int, page: Int, pageSize: Int) {
        mModel.getFollowUser(followType, page, pageSize)
    }

    fun followUser(authorId: Int) {
        mModel.followUser(authorId)
    }

    fun unFollowUser(authorId: Int) {
        mModel.unFollowUser(authorId)
    }
    override fun getEmptyFollowUserSuccess(list: MutableList<FollowUserBean>) {
        mView.getEmptyFollowUserSuccess(list)
    }

    override fun getDataSuccess(bean: MutableList<FollowUserBean>) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun postSuccess(bean: FollowUserBean) {
        mView.postSuccess(bean)
    }

    override fun unFollowUserSuccess(bean: FollowUserBean) {
        mView.unFollowUserSuccess(bean)
    }

    override fun postFail(msg: String?) {
        mView.postFail(msg)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun startLogin() {
        mView.startLogin()
    }

    override fun detachView() {
        super.detachView()
        mModel.onDestroy()
    }
}

