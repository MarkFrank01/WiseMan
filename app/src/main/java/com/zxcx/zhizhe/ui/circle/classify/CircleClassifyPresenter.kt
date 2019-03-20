package com.zxcx.zhizhe.ui.circle.classify

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
class CircleClassifyPresenter(view: CircleClassifyContract.View) : BasePresenter<CircleClassifyContract.View>(), CircleClassifyContract.Presenter {


    private val mModel: CircleClassifyModel

    init {
        attachView(view)
        mModel = CircleClassifyModel(this)
    }

    fun getCircleListByClassifyId(classifyId: Int, orderType: Int, pageIndex: Int, pageSize: Int) {
        mModel.getCircleListByClassifyId(classifyId, orderType, pageIndex, pageSize)
    }

    fun setjoinCircle(circleId:Int,joinType:Int){
        mModel.setjoinCircle(circleId,joinType)
    }

    override fun JoinCircleSuccess(bean: MutableList<CircleBean>) {
        mView.JoinCircleSuccess(bean)
    }

    override fun QuitCircleSuccess(bean: MutableList<CircleBean>) {
        mView.QuitCircleSuccess(bean)
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
        mView.toastFail(msg)
    }

    override fun postFail(msg: String?) {
        if (mView!=null) {
            mView.toastFail(msg)
        }
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>) {
        if (mView!=null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun startLogin() {
    }

}