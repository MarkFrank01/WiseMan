package com.zxcx.zhizhe.ui.circle.allmycircle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.circle.adapter.AllMyCircleAdapter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_my_circle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyJoinFragment : RefreshMvpFragment<AlllMyCirclePresenter>(), AllMyCircleContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mCreatePage = 0

    private lateinit var mAllmyCircleAdapter: AllMyCircleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_circle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        onRefresh()

        mPresenter.getRecommendCircleListWhenNoData()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    override fun createPresenter(): AlllMyCirclePresenter {
        return AlllMyCirclePresenter(this)
    }

    override fun emptyCircle(bean: MutableList<CircleBean>) {
        LogCat.e("empty size is "+bean.size)


        val emptyView = EmptyView.getEmptyViewAndCircle(mActivity, "找不到小伙伴吗？看看圈子推荐", R.drawable.no_circle_data,bean)
        mAllmyCircleAdapter.emptyView = emptyView
    }

    override fun getDataSuccess(list: MutableList<CircleBean>) {
//
//        val emptyView = EmptyView.getEmptyView(mActivity, "找不到小伙伴吗？看看圈子推荐", R.drawable.no_circle_data)
//        mAllmyCircleAdapter.emptyView = emptyView

        if (mCreatePage == 0&&list.size<1){
            mPresenter.getRecommendCircleListWhenNoData()
        }

        mRefreshLayout.finishRefresh()
        if (mCreatePage == 0) {
            mAllmyCircleAdapter.setNewData(list)
        } else {
            mAllmyCircleAdapter.addData(list)
        }

        mCreatePage++
        if (list.size < Constants.PAGE_SIZE) {
            mAllmyCircleAdapter.loadMoreEnd(false)
        } else {
            mAllmyCircleAdapter.loadMoreComplete()
            mAllmyCircleAdapter.setEnableLoadMore(false)
            mAllmyCircleAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.cb_item_select_join_circle2,R.id.iv_item_circle_classify_icon,R.id.tv_item_circle_classify_title,R.id.tv_item_circle_classify_desc -> {
                val bean = adapter.data[position] as CircleBean
                mActivity.startActivity(CircleDetaileActivity::class.java) {
                    it.putExtra("circleID", bean.id)
                }
            }

            R.id.con_click -> {
                toastShow("续费")
            }
        }
    }

    private fun initRecycleView() {
        mAllmyCircleAdapter = AllMyCircleAdapter(ArrayList())
        mAllmyCircleAdapter.onItemClickListener = this
        mAllmyCircleAdapter.onItemChildClickListener = this

        mAllmyCircleAdapter.setLoadMoreView(CustomLoadMoreView())
        mAllmyCircleAdapter.setOnLoadMoreListener(this,rv_my_circle_all)

        rv_my_circle_all.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_my_circle_all.adapter = mAllmyCircleAdapter


    }

    fun onRefresh() {
        getMyCreate()
    }

    //获取我创建
    private fun getMyCreate() {
        mPresenter.getMyJoin(mCreatePage, Constants.PAGE_SIZE)
    }

}