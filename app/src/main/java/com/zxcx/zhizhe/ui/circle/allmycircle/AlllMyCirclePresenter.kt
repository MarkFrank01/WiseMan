package com.zxcx.zhizhe.ui.circle.allmycircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AlllMyCirclePresenter(view:AllMyCircleContract.View):BasePresenter<AllMyCircleContract.View>(),AllMyCircleContract.Presenter{


    private val mModel : AllMyCircleModel

    init {
        attachView(view)
        mModel = AllMyCircleModel(this)
    }

    fun getMyJoin(page:Int,pageSize:Int){
        mModel.getMyJoinCircleList(page,pageSize)
    }

    fun getMyCreate(page: Int,pageSize: Int){
        mModel.getMyCreateCircleList(page,pageSize)
    }

    fun getRecommendCircleListWhenNoData(){
        mModel.getRecommendCircleListWhenNoData()
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.showLoading()
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        mView.getDataSuccess(bean)
    }

    override fun emptyCircle(bean: MutableList<CircleBean>) {
        mView.emptyCircle(bean)
    }

    override fun startLogin() {
        mView.startLogin()
    }

}