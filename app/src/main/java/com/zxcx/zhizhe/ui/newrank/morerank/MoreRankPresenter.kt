package com.zxcx.zhizhe.ui.newrank.morerank

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean

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

    fun followUser(authorId: Int,position:Int) {
        mModel.followUser(authorId,position)
    }

    fun unFollowUser(authorId: Int,position:Int) {
        mModel.unFollowUser(authorId,position)
    }

    override fun followUserSuccess(bean: UserRankBean,position: Int) {
        if (mView != null) {
            mView.followUserSuccess(bean,position)
        }
    }

    override fun unFollowUserSuccess(bean: UserRankBean,position: Int) {
        if (mView != null) {
            mView.unFollowUserSuccess(bean,position)
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

    override fun postSuccess(bean: UserRankBean?) {
    }

    override fun postFail(msg: String?) {
    }

}