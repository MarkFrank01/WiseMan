package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/26
 * @Description :
 */
class CreateCirclePresenter(view: CreateCircleContract.View) : BasePresenter<CreateCircleContract.View>(), CreateCircleContract.Presenter {


    private val mModel: CreateCircleModel

    init {
        attachView(view)
        mModel = CreateCircleModel(this)
    }

    fun createCircle(title: String, titleImage: String, classifyId: Int, sign: String, price: String, articleList: List<Int>, levelType: Int) {
        mModel.createCircle(title, titleImage, classifyId, sign, price, articleList, levelType)
    }

    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String, levelType: Int, limitedTimeType: Int) {
        mModel.createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
    }


    fun checkName(name: String) {
        mModel.checkCircleName(name)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun checkSuccess() {
        mView.checkSuccess()
    }

    override fun getDataSuccess(bean: CircleBean) {
        mView.getDataSuccess(bean)
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun startLogin() {
    }

}