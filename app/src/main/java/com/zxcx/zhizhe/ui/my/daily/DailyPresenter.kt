package com.zxcx.zhizhe.ui.my.daily

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyPresenter(view: DailyContract.View) : BasePresenter<DailyContract.View>(), DailyContract.Presenter {


    private val mModel: DailyModel

    init {
        attachView(view)
        mModel = DailyModel(this)
    }

    fun getDailyList(termIndex: Int) {
        mModel.getDailyList(termIndex)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<DailyBean>?) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun startLogin() {
    }

}