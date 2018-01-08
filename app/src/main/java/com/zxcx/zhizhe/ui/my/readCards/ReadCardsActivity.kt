package com.zxcx.zhizhe.ui.my.readCards

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_read_cards.*
import kotlinx.android.synthetic.main.toolbar.*

class ReadCardsActivity : MvpActivity<ReadCardsPresenter>(), ReadCardsContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private var mSortType = 0//0倒序 1正序
    private lateinit var mAdapter: ReadCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_cards)
        initView()
        initToolBar("阅读")
        initLoadSir()
        mPresenter.getReadCard(mSortType,mPage,mPageSize)
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(rv_read_card, this)
    }

    override fun createPresenter(): ReadCardsPresenter {
        return ReadCardsPresenter(this)
    }

    private fun getReadCard() {
        mPresenter.getReadCard(mSortType, mPage, Constants.PAGE_SIZE)
    }

    override fun getDataSuccess(list: List<ReadCardsBean>) {
        if (mPage == 0) {
            loadService.showSuccess()
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getReadCard(mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as ReadCardsBean
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initView() {
        mAdapter = ReadCardsAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_read_card)
        rv_read_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_read_card.adapter = mAdapter
        rv_read_card.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无阅读历史", "快去看看你喜欢什么", null, null)
        mAdapter.emptyView = emptyView

        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_order_sequence)
        iv_toolbar_right.setOnClickListener {
            if (mSortType == 1) {
                mSortType = 0
                iv_toolbar_right.setImageResource(R.drawable.iv_order_sequence)
            } else if (mSortType == 0) {
                mSortType = 1
                iv_toolbar_right.setImageResource(R.drawable.iv_order_inverted)
            }
            mPage = 0
            getReadCard() }
    }
}
