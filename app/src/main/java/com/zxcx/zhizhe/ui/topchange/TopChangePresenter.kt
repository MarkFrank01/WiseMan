package com.zxcx.zhizhe.ui.topchange

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
class TopChangePresenter(view:TopChangeContract.View):BasePresenter<TopChangeContract.View>(),TopChangeContract.Presenter {

    private val mModel :TopChangeModel

    init {
        attachView(view)
        mModel = TopChangeModel(this)
    }

    fun setClassifyMenu(classifyList:MutableList<Int>){
        mModel.setClassifyMenu(classifyList)
    }

    fun getAllNavClassify(){
        mModel.getAllNavClassify()
    }

    override fun setClassifyMenuSuccess() {
        if (mView!=null){
            mView.setClassifyMenuSuccess()
        }
    }

    override fun getAllNavClassifySuccess(list: MutableList<ClassifyBean>) {
        if (mView!=null){
            mView.getAllNavClassifySuccess(list)
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

    override fun getDataSuccess(bean: MutableList<ClassifyBean>?) {
    }

    override fun startLogin() {
    }

}