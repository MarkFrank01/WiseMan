package com.zxcx.zhizhe.pay.zfb

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/4/1
 * @Description :
 */
class ZFBPresenter(view: ZFBContract.View) : BasePresenter<ZFBContract.View>(), ZFBContract.Presenter {


    private val mModel: ZFBModel

    init {
        attachView(view)
        mModel = ZFBModel(this)
    }

    fun getAlOrderPayForJoinCircle(circleId: Int) {
        mModel.getAlOrderPayForJoinCircle(circleId)
    }

    override fun getAlOrderPayForJoinCircle(key: String) {
        if (mView != null) {
            mView.getAlOrderPayForJoinCircle(key)
        }
    }

    override fun postSuccess(bean: String?) {
    }



    override fun postFail(msg: String?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }


    override fun startLogin() {
    }

}