package com.zxcx.zhizhe.ui.circle.circlemessage.question

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesQaPresenter(view: CircleMesQaContract.View) : BasePresenter<CircleMesQaContract.View>(), CircleMesQaContract.Presenter {

    private val mModel: CircleMesQaModel

    init {
        attachView(view)
        mModel = CircleMesQaModel(this)
    }

    fun getQuestionMessageList(page: Int, pageSize: Int) {
        mModel.getQuestionMessageList(page, pageSize)
    }

    override fun getQuestionMessageListSuccess(bean: MutableList<MyCircleTabBean>) {
        if (mView != null) {
            mView.getQuestionMessageListSuccess(bean)
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