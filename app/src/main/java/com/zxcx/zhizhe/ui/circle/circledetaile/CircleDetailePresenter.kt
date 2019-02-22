package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetailePresenter(view: CircleDetaileContract.View) : BasePresenter<CircleDetaileContract.View>(), CircleDetaileContract.Presenter {


    private val mModel: CircleDetaileModel

    init {
        attachView(view)
        mModel = CircleDetaileModel(this)
    }

    fun getCircleBasicInfo(circleId: Int) {
        mModel.getCircleBasicInfo(circleId)
    }

    fun getCircleMemberByCircleId(orderType: Int, circleId: Int, pageIndex: Int, pageSize: Int) {
        mModel.getCircleMemberByCircleId(orderType, circleId, pageIndex, pageSize)
    }

    fun getCircleQAByCircleId(orderType: Int, circleId: Int, pageIndex: Int, pageSize: Int) {
        mModel.getCircleQAByCircleId(orderType, circleId, pageIndex, pageSize)
    }

    override fun getCircleBasicInfoSuccess(bean: CircleBean) {
        mView.getCircleBasicInfoSuccess(bean)
    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
        mView.getCircleMemberByCircleIdSuccess(bean)
    }

    override fun getCircleQAByCircleIdSuccess(bean: MutableList<CircleDetailBean>) {
        mView.getCircleQAByCircleIdSuccess(bean)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}