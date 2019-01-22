package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CirclePresenter(view: CircleContract.View):BasePresenter<CircleContract.View>(), CircleContract.Presenter {

    private val mModel: CircleModel

    init {
        attachView(view)
        mModel = CircleModel(this)
    }

    fun getAD(){
        mModel.getAD()
    }

    fun getClassify(page:Int,pageSize:Int){
        mModel.getClassify(page,pageSize)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        mView.getADSuccess(list)
    }

    override fun getClassifySuccess(list: MutableList<CircleClassifyBean>) {
        mView.getClassifySuccess(list)
    }


    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun startLogin() {
    }

}