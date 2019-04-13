package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.bean.CheckBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/4
 * @Description :
 */
class CirclePingFenPresenter(view: CirclePingFenContract.View) : BasePresenter<CirclePingFenContract.View>(), CirclePingFenContract.Presenter {

    private val mModel: CirclePingFenModel

    init {
        attachView(view)
        mModel = CirclePingFenModel(this)
    }

    fun evaluationCircle(circleId: Int, contentQuality: Int, topicQuality: Int) {
        mModel.evaluationCircle(circleId, contentQuality, topicQuality)
    }

    override fun evaluationCircleSuccess() {
        if (mView != null) {
            mView.evaluationCircleSuccess()
        }
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

    override fun getDataSuccess(bean: CheckBean?) {
    }

    override fun startLogin() {
    }

}