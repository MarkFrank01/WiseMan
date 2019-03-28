package com.zxcx.zhizhe.ui.my.selectAttention.man

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import kotlinx.android.synthetic.main.activity_now_select_man.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectManActivity:MvpActivity<NowSelectManPresenter>(),NowSelectManContract.View {

    private lateinit var mAdapter:NowSelectManAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_select_man)
        initView()
        initRecycleView()

        mPresenter.getInterestRecommendForUser()
    }

    override fun createPresenter(): NowSelectManPresenter {
        return NowSelectManPresenter(this)
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<SearchUserBean>?) {
        mAdapter.setNewData(bean)
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            toastShow("设置成功")
            finish()
        }
    }

    private fun initView(){
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.button_blue))

    }

    private fun initRecycleView(){
        mAdapter = NowSelectManAdapter(ArrayList())

        val manage = GridLayoutManager(mActivity,3)

        rv_select_man.adapter = mAdapter
        rv_select_man.layoutManager = manage

        mAdapter.setOnItemChildClickListener { adapter, view, position ->

        }
    }
}