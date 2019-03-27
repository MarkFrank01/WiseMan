package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailPresenter(view: CircleManDetailContract.View) : BasePresenter<CircleManDetailContract.View>(), CircleManDetailContract.Presenter {


    private val mModel: CircleManDetailModel

    init {
        attachView(view)
        mModel = CircleManDetailModel(this)
    }

    fun getCircleListByAuthorId(page: Int, pageSize: Int, userId: Int) {
        mModel.getCircleListByAuthorId(page, pageSize, userId)
    }

    fun getAuthorInfo(userId: Int) {
        mModel.getAuthorInfo(userId)
    }

    fun getOtherUserCreationSuccess(userId: Int, orderType: Int, page: Int, pageSize: Int) {
        mModel.getOtherUserCreationSuccess(userId, orderType, page, pageSize)
    }

    fun setUserFollow(authorId: Int, followType: Int) {
        mModel.setUserFollow(authorId, followType)
    }

    override fun getCircleListByAuthorIdSuccess(list: MutableList<CircleBean>) {
        if (mView != null) {
            mView.getCircleListByAuthorIdSuccess(list)
        }
    }

    override fun getAuthorInfoSuccess(bean: CircleUserBean) {
        if (mView != null) {
            mView.getAuthorInfoSuccess(bean)
        }
    }

    override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
        if (mView != null) {
            mView.getOtherUserCreationSuccess(list)
        }
    }

    override fun followSuccess() {
        if (mView != null) {
            mView.followSuccess()
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: CircleUserBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}