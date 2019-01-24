package com.zxcx.zhizhe.ui.circle.mycircle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.MyCircleCreateAdapter
import com.zxcx.zhizhe.ui.circle.adapter.MyCircleJoinAdapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_mycircle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class MyCircleActivity : RefreshMvpActivity<MyCirclePresenter>(), MyCircleContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mJoinPage = 0
    private var mCreatePage = 0

    private lateinit var mCircleCreateAdapter: MyCircleCreateAdapter
    private lateinit var mCircleJoinAdapter: MyCircleJoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycircle)
        initView()

        onRefresh()

    }

    override fun createPresenter(): MyCirclePresenter {
        return MyCirclePresenter(this)
    }

    override fun getMyJoinSuccess(list: MutableList<CircleBean>) {
        LogCat.e("MyJOin ${list.size}")
        mRefreshLayout.finishRefresh()
        if (mJoinPage == 0) {
            mCircleJoinAdapter.setNewData(list)
        } else {
            mCircleJoinAdapter.addData(list)
        }
        mJoinPage++
        if (list.size < Constants.PAGE_SIZE) {
            mCircleJoinAdapter.loadMoreEnd(false)
        } else {
            mCircleJoinAdapter.loadMoreComplete()
            mCircleJoinAdapter.setEnableLoadMore(false)
            mCircleJoinAdapter.setEnableLoadMore(true)
        }
    }

    override fun getMyCreateSuccess(list: MutableList<CircleBean>) {
        LogCat.e("myCreate ${list.size}")
        if (mCreatePage == 0) {
            mCircleCreateAdapter.setNewData(list)
            if (list.size < 1) {
                empty.visibility = View.VISIBLE
            }
        } else {
            mCircleCreateAdapter.addData(list)
        }
        mCreatePage++
        if (list.size < Constants.PAGE_SIZE) {
            mCircleCreateAdapter.loadMoreEnd(false)
        } else {
            mCircleCreateAdapter.loadMoreComplete()
            mCircleCreateAdapter.setEnableLoadMore(false)
            mCircleCreateAdapter.setEnableLoadMore(true)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun setListener() {
        empty.setOnClickListener {

        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

    private fun initView() {
        initToolBar("我的圈子")

        mCircleCreateAdapter = MyCircleCreateAdapter(ArrayList())
        mCircleJoinAdapter = MyCircleJoinAdapter(ArrayList())

        rv_my_circle.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_my_circle.adapter = mCircleCreateAdapter

        rv_my_circle2.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_my_circle2.adapter = mCircleJoinAdapter

//        val emptyView = EmptyView.getEmptyView2(mActivity, "暂无内容", R.drawable.no_data)
//        mCircleCreateAdapter.emptyView = emptyView
        val emptyView2 = EmptyView.getEmptyView2(mActivity, "暂无内容", R.drawable.no_data)
        mCircleJoinAdapter.emptyView = emptyView2
    }

    fun onRefresh() {
        getMyJoin()
        getMyCreate()
    }

    //获取我加入
    private fun getMyJoin() {
        mPresenter.getMyJoin(mJoinPage, Constants.PAGE_SIZE)
    }

    //获取我创建
    private fun getMyCreate() {
        mPresenter.getMyCreate(mCreatePage, Constants.PAGE_SIZE)
    }
}