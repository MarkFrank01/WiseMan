package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class CircleEditPresenter(view: CircleEditContract.View) : BasePresenter<CircleEditContract.View>(), CircleEditContract.Presenter {

    private val mModel: CircleEditModel

    init {
        attachView(view)
        mModel = CircleEditModel(this)
    }

    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String, levelType: Int, limitedTimeType: Int) {
        mModel.createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
    }

    override fun checkSuccess() {
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: CircleBean?) {
    }

    override fun startLogin() {
    }


}