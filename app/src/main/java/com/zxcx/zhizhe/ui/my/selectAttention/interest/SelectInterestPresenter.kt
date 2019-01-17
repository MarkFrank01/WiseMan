package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectInterestPresenter(view:SelectInterestContract.View):BasePresenter<SelectInterestContract.View>(),SelectInterestContract.Presenter {

    private val mModel:SelectInterestModel

    init {
        attachView(view)
        mModel = SelectInterestModel(this)
    }

    fun getInterestRecommend(){
        mModel.getInterestRecommend()
    }

    fun changeAttentionList(idList:MutableList<Int>){
        mModel.changeAttentionList(idList)
    }

    override fun getDataSuccess(bean: InterestRecommendBean) {
        mView.getDataSuccess(bean)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun postSuccess() {
        mView.postSuccess()
    }


    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun postFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun startLogin() {
    }

}