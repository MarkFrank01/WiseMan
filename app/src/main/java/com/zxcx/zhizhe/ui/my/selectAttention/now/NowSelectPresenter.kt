package com.zxcx.zhizhe.ui.my.selectAttention.now

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectPresenter(view: NowSelectContract.View) : BasePresenter<NowSelectContract.View>(), NowSelectContract.Presenter {


    private val mModel: NowSelectModel

    init {
        attachView(view)
        mModel = NowSelectModel(this)
    }

    fun getInterestRecommendForClassify() {
        mModel.getInterestRecommendForClassify()
    }

    fun followClassify(classifyList: MutableList<Int>) {
        mModel.followClassify(classifyList)
    }

    override fun showLoading() {
    }

    override fun postSuccess() {
    }

    override fun hideLoading() {
    }

    override fun getDataFail(msg: String?) {
        if (mView != null) {
        }
    }

    override fun postFail(msg: String?) {
    }

    override fun followClassifySuccess() {
        if (mView != null) {
            mView.followClassifySuccess()
        }
    }

    override fun getDataSuccess(bean: MutableList<ClassifyBean>?) {
        if (mView != null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun startLogin() {
        2
    }


}