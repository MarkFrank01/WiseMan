package com.zxcx.zhizhe.ui.my.daily

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.hot.DailyAdapter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_daily.*
import kotlinx.android.synthetic.main.activity_label.*

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyActivity : RefreshMvpActivity<DailyPresenter>(), DailyContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {


    private lateinit var mAdapter: DailyAdapter
    private var mPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        initToolBar("实用头条")
        initView()
        onRefresh()

        initLoadSir()
    }

    override fun createPresenter(): DailyPresenter {
        return DailyPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>) {
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            mAdapter.setNewData(bean)
        } else {
            mAdapter.addData(bean)
        }
        mPage++
        if (bean.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    private fun initView() {
        mAdapter = DailyAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_card_bag_card)

        rv_daily_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_daily_card.adapter = mAdapter

//        mPresenter.getDailyList(0)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
        val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
        val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
        val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair.create(cardImg, cardImg.transitionName),
                Pair.create(cardTitle, cardTitle.transitionName),
                Pair.create(cardCategory, cardCategory.transitionName),
                Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("list", mAdapter.data as java.util.ArrayList)
        intent.putExtra("currentPosition", position)
        intent.putExtra("sourceName", this::class.java.name)
        mActivity.startActivity(intent, bundle)
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh()
    }

    override fun onLoadMoreRequested() {
        mPage += 1
        getDailyCard(mPage)
    }

    fun onRefresh() {
        getDailyCard(mPage)
    }

    private fun getDailyCard(page: Int) {
        mPresenter.getDailyList(page)
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
}