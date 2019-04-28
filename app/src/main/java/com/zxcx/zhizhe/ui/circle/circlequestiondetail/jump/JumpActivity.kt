package com.zxcx.zhizhe.ui.circle.circlequestiondetail.jump

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlequestiondetail.CircleCommentBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/28
 * @Description :
 */
class JumpActivity : MvpActivity<JumpPresenter>(), JumpContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    override fun createPresenter(): JumpPresenter {
        return JumpPresenter(this)
    }

    override fun likeSuccess() {
    }

    override fun unlikeSuccess() {
    }

    override fun postSuccess(bean: CircleCommentBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleCommentBean>?) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

}