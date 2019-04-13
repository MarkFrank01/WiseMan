package com.zxcx.zhizhe.ui.circle.circlemessage.zan

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesZanPresenter(view: CircleMesZanContract.View) : BasePresenter<CircleMesZanContract.View>(), CircleMesZanContract.Presenter {

    private val mModel: CircleMesZanModel

    init {
        attachView(view)
        mModel = CircleMesZanModel(this)
    }

    fun getLikeMessageList(page: Int, pageSize: Int) {
        mModel.getLikeMessageList(page, pageSize)
    }

    override fun getLikeMessageListSuccess(bean: MutableList<MyCircleTabBean>) {
        if (mView!=null){
            mView.getLikeMessageListSuccess(bean)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MyCircleTabBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}