package com.zxcx.zhizhe.ui.circle.classify

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleClassifyAdapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_circle_tuijian.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/6
 * @Description :
 */
class CircleTuiActivity : RefreshMvpActivity<CircleTuiPresenter>(), CircleTuiContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mCircleListPage = 0
    private var mCircleListPageSize = 10

    private lateinit var mCircleTuiAdapter: CircleClassifyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_tuijian)
        initView()

        getCircleById()

        initLoadSir()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getCircleById()
    }

    override fun createPresenter(): CircleTuiPresenter {
        return CircleTuiPresenter(this)
    }

    override fun JoinCircleSuccess(bean: MutableList<CircleBean>) {
    }

    override fun postSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>) {
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (mCircleListPage == 0) {
            mCircleTuiAdapter.setNewData(bean)
        } else {
            mCircleTuiAdapter.addData(bean)
        }

        mCircleListPage++
        if (bean.size < Constants.PAGE_SIZE) {
            mCircleTuiAdapter.loadMoreEnd(false)
        } else {
            mCircleTuiAdapter.loadMoreComplete()
            mCircleTuiAdapter.setEnableLoadMore(false)
            mCircleTuiAdapter.setEnableLoadMore(true)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.cb_item_select_join_circle -> {
                val bean = adapter.data[position] as CircleBean
                bean.hasJoin = !bean.hasJoin
                mCircleTuiAdapter.notifyItemChanged(position)

                if (bean.hasJoin) {
                    //加入圈子
//                    mPresenter.setjoinCircle(bean.id, 0)
                }
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
        getCircleById()
    }

    private fun initLoadSir() {
        val loadSir = LoadSir.Builder()
                .addCallback(LoadingCallback())
                .addCallback(NetworkErrorCallback())
                .addCallback(LoginTimeoutCallback())
                .setDefaultCallback(LoadingCallback::class.java)
                .build()
        loadService = loadSir.register(mRefreshLayout, this)
    }

    private fun initView() {
        initToolBar("推荐")

        mCircleTuiAdapter = CircleClassifyAdapter(ArrayList())
        mCircleTuiAdapter.onItemClickListener = this
        mCircleTuiAdapter.onItemChildClickListener = this

        val view = EmptyView.getEmptyView(mActivity, "暂无圈子", R.drawable.no_circle_data)
        mCircleTuiAdapter.emptyView = view

        mCircleTuiAdapter.setLoadMoreView(CustomLoadMoreView())
        mCircleTuiAdapter.setOnLoadMoreListener(this,rv_circle_classify)

        rv_circle_classify.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_circle_classify.adapter = mCircleTuiAdapter
    }


    //获取推荐进来的信息
    private fun getCircleById() {
        mPresenter.getRecommendCircleListByPage(mCircleListPage, mCircleListPageSize)
    }
}