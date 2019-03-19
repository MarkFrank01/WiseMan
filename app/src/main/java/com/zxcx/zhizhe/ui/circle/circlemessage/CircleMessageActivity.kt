package com.zxcx.zhizhe.ui.circle.circlemessage

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_circle_message.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleMessageActivity : MvpActivity<CircleMessagePresenter>(), CircleMessageContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_message)

        initView()
    }

    override fun createPresenter(): CircleMessagePresenter {
        return CircleMessagePresenter(this)
    }

    override fun getRedPointStatusSuccess(bean: MyCircleTabBean) {
        if (bean.hasCircleQuestionDynamicMessage) {
            iv_tab_red_point_1.visibility = View.VISIBLE
        } else {
            iv_tab_red_point_1.visibility = View.GONE
        }

        if (bean.hasCircleLikeDynamicMessage) {
            iv_tab_red_point_2.visibility = View.VISIBLE
        } else {
            iv_tab_red_point_2.visibility = View.GONE
        }
    }

    override fun getCommentMessageListSuccess(list: MutableList<MyCircleTabBean>) {
        LogCat.e("MessageList" + list.size)
    }

    override fun getDataSuccess(bean: MyCircleTabBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }


    private fun initView() {
        initToolBar("圈子动态")
        mPresenter.getCircleTabInfo()
        mPresenter.getCommentMessageList(page, Constants.PAGE_SIZE)
    }


//    override fun onResume() {
//        super.onResume()
//        if (SharedPreferencesUtil.getInt(SVTSConstants.userId,0)!= 0){
//            getRedPointStatus()
//        }
//    }
//

}