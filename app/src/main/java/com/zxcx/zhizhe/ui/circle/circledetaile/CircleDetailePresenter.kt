package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.my.money.MoneyBean

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

    fun joinCircleByZzbForAndroid(circleId: Int) {
        mModel.joinCircleByZzbForAndroid(circleId)
    }

    fun freeAddCircle(circleId: Int){
        mModel.freeAddCircle(circleId)
    }

    fun getAccountDetails() {
        mModel.getAccountDetails()
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

    fun reportCircle(circleId: Int, reportType: Int) {
        mModel.reportCircle(circleId, reportType)
    }

    fun setQAFixTop(qaId: Int, fixType: Int) {
        mModel.setQAFixTop(qaId, fixType)
    }

    fun deleteQa(qaId: Int) {
        mModel.deleteQa(qaId)
    }

    override fun deleteQaSuccess() {
        mView.deleteQaSuccess()
    }


    override fun setQAFixTopSuccess() {
        mView.setQAFixTopSuccess()
    }

    override fun reportCircleSuccess() {
        mView.reportCircleSuccess()
    }


    override fun getCircleBasicInfoSuccess(bean: CircleBean) {
        if (mView!=null) {
            mView.getCircleBasicInfoSuccess(bean)
        }
    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
        if (mView!=null) {
            mView.getCircleMemberByCircleIdSuccess(bean)
        }
    }

    override fun getCircleQAByCircleIdSuccess(bean: MutableList<CircleDetailBean>) {
        if (mView != null) {
            mView.getCircleQAByCircleIdSuccess(bean)
        }
    }

    override fun joinCircleByZzbForAndroidSuccess() {
        if (mView != null) {
            mView.joinCircleByZzbForAndroidSuccess()
        }
    }

    override fun getAccountDetailsSuccess(bean: MoneyBean) {
        if (mView != null) {
            mView.getAccountDetailsSuccess(bean)
        }
    }

    override fun postSuccess() {
        mView.postSuccess()
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

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
        mView.startLogin()
    }

}