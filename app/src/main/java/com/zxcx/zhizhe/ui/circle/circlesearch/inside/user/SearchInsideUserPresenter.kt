package com.zxcx.zhizhe.ui.circle.circlesearch.inside.user

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideUserPresenter(view: SearchInsideUserContract.View) : BasePresenter<SearchInsideUserContract.View>(), SearchInsideUserContract.Presenter {

    private val mModel: SearchInsideUserModel

    init {
        attachView(view)
        mModel = SearchInsideUserModel(this)
    }

    fun searchUser(page: Int, pageSIze: Int, circleId: Int, keyword: String) {
        mModel.searchUser(page, pageSIze, circleId, keyword)
    }

    fun followUser(authorId: Int) {
        mModel.followUser(authorId)
    }

    fun unFollowUser(authorId: Int) {
        mModel.unFollowUser(authorId)
    }

    override fun getDataSuccess(bean: List<SearchUserBean>) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun postSuccess(bean: SearchUserBean) {
        mView.postSuccess(bean)
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        mView.unFollowUserSuccess(bean)
    }

    override fun postFail(msg: String) {
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