package com.zxcx.zhizhe.ui.newrank.morerank

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankPresenter(view: MoreRankContract.View) : BasePresenter<MoreRankContract.View>(), MoreRankContract.Presenter {


    private val mModel: MoreRankModel

    init {
        attachView(view)
        mModel = MoreRankModel(this)
    }

    fun getMoreRank(page: Int, pageSize: Int) {
        mModel.getMoreRank(page, pageSize)
    }

    fun followUser(authorId: Int) {
        mModel.followUser(authorId)
    }

    fun unFollowUser(authorId: Int) {
        mModel.unFollowUser(authorId)
    }

    override fun followUserSuccess(bean: SearchUserBean) {
        if (mView != null) {
            mView.followUserSuccess(bean)
        }
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        if (mView != null) {
            mView.unFollowUserSuccess(bean)
        }
    }

    override fun getMoreRankSuccess(list: List<UserRankBean>) {
        if (mView != null) {
            mView.getMoreRankSuccess(list)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}