package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleNamePresenter(view:CreateCircleNameContract.View):BasePresenter<CreateCircleNameContract.View>(),CreateCircleNameContract.Presenter {


    private val mModel:CreateCircleNameModel

    init {
        attachView(view)
        mModel = CreateCircleNameModel(this)
    }

    fun checkName(name:String){
        mModel.checkCircleName(name)
    }

    override fun checkCanUse() {
        mView.checkCanUse()
    }

    override fun checkSuccess() {
        mView.checkSuccess()
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