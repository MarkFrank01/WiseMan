package com.zxcx.zhizhe.ui.circle.classify

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/6
 * @Description :
 */
class CircleTuiPresenter(view: CircleTuiContract.View) : BasePresenter<CircleTuiContract.View>(), CircleTuiContract.Presenter {

    private val mModel: CircleTuiModel

    init {
        attachView(view)
        mModel = CircleTuiModel(this)
    }

    fun getRecommendCircleListByPage(page: Int, pageSize: Int) {
        mModel.getRecommendCircleListByPage(page, pageSize)
    }

    fun setjoinCircle(circleId: Int, joinType: Int) {
        mModel.setjoinCircle(circleId, joinType)
    }

    override fun JoinCircleSuccess(bean: MutableList<CircleBean>) {
        mView.JoinCircleSuccess(bean)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun postSuccess(bean: MutableList<CircleBean>) {
        mView.postSuccess(bean)
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>) {
        mView.getDataSuccess(bean)
    }

    override fun startLogin() {
    }

}