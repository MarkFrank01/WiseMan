package com.zxcx.zhizhe.ui.my

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean
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

    fun getInvitationInfo() {
        mModel.getInvitationInfo()
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        if (mView!=null) {
            mView.getADSuccess(list)
        }
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<MyTabBean>?) {
    }

    override fun getInvitationInfoSuccess(bean: InviteBean) {
        if (mView!=null){
            mView.getInvitationInfoSuccess(bean)
        }
    }

    override fun ewmError(msg: String) {
        if (mView!=null){
            mView.ewmError(msg)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
        mView.startLogin()
    }
}