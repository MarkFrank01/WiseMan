package com.zxcx.zhizhe.ui.my.selectAttention.man

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectManPresenter(view:NowSelectManContract.View):BasePresenter<NowSelectManContract.View>(),NowSelectManContract.Presenter {

    private val mModel:NowSelectManModel

    init {
        attachView(view)
        mModel = NowSelectManModel(this)
    }

    fun getInterestRecommendForUser(){
        mModel.getInterestRecommendForUser()
    }

    fun followUser(authorId: Int) {
        mModel.followUser(authorId)
    }

    fun unFollowUser(authorId: Int) {
        mModel.unFollowUser(authorId)
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
        mView.mFollowUserSuccess(bean)
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        mView.unFollowUserSuccess(bean)
    }

    override fun showLoading() {
    }

    override fun postSuccess() {
    }

    override fun hideLoading() {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<SearchUserBean>?) {
        if (mView!=null){
            mView.getDataSuccess(bean)
        }
    }

    override fun startLogin() {
    }

}