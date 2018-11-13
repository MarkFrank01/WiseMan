package com.zxcx.zhizhe.ui.card

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class HomeCardPresenter(view: HomeCardContract.View) : BasePresenter<HomeCardContract.View>(), HomeCardContract.Presenter {


    private val mModel: HomeCardModel

    init {
        attachView(view)
        mModel = HomeCardModel(this)
    }

    fun getAD(lastOpenedTime: Long, lastOpenedAdId: Long) {
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

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

}