package com.zxcx.zhizhe.ui.circle.circlemanlist

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleManListPresenter(view:CircleManListContract.View):BasePresenter<CircleManListContract.View>(),CircleManListContract.Presnter{

    private val mModel:CircleManListModel

    init {
        attachView(view)
        mModel = CircleManListModel(this)
    }


    fun followUser(authorId:Int){
        mModel.followUser(authorId)
    }

    fun unFollowUser(authorId: Int){
        mModel.unFollowUser(authorId)
    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
        mView.getCircleMemberByCircleIdSuccess(bean)
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
        mView.mFollowUserSuccess(bean)
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        mView.unFollowUserSuccess(bean)
    }


    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun startLogin() {
    }


}