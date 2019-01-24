package com.zxcx.zhizhe.ui.circle.mycircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class MyCirclePresenter(view:MyCircleContract.View):BasePresenter<MyCircleContract.View>(),MyCircleContract.Presenter{

    private val mModel : MyCircleModel

    init {
        attachView(view)
        mModel = MyCircleModel(this)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    fun getMyJoin(page:Int,pageSize:Int){
        mModel.getMyJoinCircleList(page,pageSize)
    }

    fun getMyCreate(page: Int,pageSize: Int){
        mModel.getMyCreateCircleList(page,pageSize)
    }

    override fun getMyJoinSuccess(list: MutableList<CircleBean>) {
        mView.getMyJoinSuccess(list)
    }

    override fun getMyCreateSuccess(list: MutableList<CircleBean>) {
        mView.getMyCreateSuccess(list)
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun startLogin() {
    }

}