package com.zxcx.zhizhe.ui.my

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class MyFragmentPresenter(view:MyFragmentContract.View): BasePresenter<MyFragmentContract.View>(),MyFragmentContract.Presenter{


    private val mModel:MyFragmentModel

    init {
        attachView(view)
        mModel = MyFragmentModel(this)
    }

    fun getAD(lastOpenedTime: Long, lastOpenedAdId: Long){
        mModel.getAD(lastOpenedTime, lastOpenedAdId)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        mView.getADSuccess(list)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<MyTabBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }
}