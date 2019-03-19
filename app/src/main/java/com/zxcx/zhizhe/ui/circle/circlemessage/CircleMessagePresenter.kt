package com.zxcx.zhizhe.ui.circle.circlemessage

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */

class CircleMessagePresenter(view: CircleMessageContract.View) : BasePresenter<CircleMessageContract.View>(), CircleMessageContract.Presenter {

    private val mModel: CircleMessageModel

    init {
        attachView(view)
        mModel = CircleMessageModel(this)
    }

    fun getCircleTabInfo() {
        mModel.getCircleTabInfo()
    }

    fun getCommentMessageList(page: Int, pageSize: Int) {
        mModel.getCommentMessageList(page, pageSize)
    }

    override fun getRedPointStatusSuccess(bean: MyCircleTabBean) {
        if (mView != null) {
            mView.getRedPointStatusSuccess(bean)
        }
    }

    override fun getCommentMessageListSuccess(bean: MutableList<MyCircleTabBean>) {
        if (mView != null) {
            mView.getCommentMessageListSuccess(bean)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MyCircleTabBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}