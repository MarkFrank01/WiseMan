package com.zxcx.zhizhe.ui.circle.circledetaile.recommend

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.BottomListPopup.CirclePopup
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_circle_recommend.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/18
 * @Description :
 */
class CircleRecommendActivity : RefreshMvpActivity<CircleRecommendPresenter>(), CircleRecommendContract.View, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private var circleID: Int = 0
    private var mSelectPosition = 0
    private var page = 0
    private var type = 0

    private lateinit var mAdapter: CircleRecommendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_recommend)
        initData()
        initView()
        initLoadSir()


        onRefresh()

    }

    override fun onReload(v: View) {
        loadService.showCallback(LoadingCallback::class.java)
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh()
    }

    override fun createPresenter(): CircleRecommendPresenter {
        return CircleRecommendPresenter(this)
    }

    override fun getRecommendArcSuccess(list: MutableList<CardBean>) {

        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (page == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        page++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        if (bean.cardType == 1){
            mActivity.startActivity(SingleCardDetailsActivity::class.java) {
                it.putExtra("cardBean", bean)
            }
        }else if (bean.cardType == 2){
            mActivity.startActivity(ArticleDetailsActivity::class.java){
                it.putExtra("cardBean",bean)
            }
        }
    }

    override fun onLoadMoreRequested() {
        getRecommendArc()
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

    private fun initData() {
        circleID = intent.getIntExtra("circleID", 0)

    }

    private fun initView() {
        initToolBar("精选内容")
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.c_more_2)

        mAdapter = CircleRecommendAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_circle_recommend)
        mAdapter.onItemChildClickListener = this

        rv_circle_recommend.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_circle_recommend.adapter = mAdapter
    }

    override fun setListener() {
        iv_toolbar_right.setOnClickListener {
            order()
        }
    }

    fun order() {
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "筛选查看", arrayOf("默认排序", "只看卡片", "只看长文"),
                        null, mSelectPosition,
                        OnSelectListener { position, text ->
                            mSelectPosition = position
                            onRefresh()
                        })
                ).show()
    }

    fun onRefresh() {
        page = 0
        getRecommendArc()
    }

    fun getRecommendArc() {
        mPresenter.getArticleByCircleId(circleID, mSelectPosition, page, Constants.PAGE_SIZE)
    }

}