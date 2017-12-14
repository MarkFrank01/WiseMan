package com.zxcx.zhizhe.ui.my.likeCards

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.search.result.card.SearchCardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_like_cards.*
import kotlinx.android.synthetic.main.toolbar.*

class LikeCardsActivity : MvpActivity<LikeCardsPresenter>(), LikeCardsContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private var mSortType = 1//0倒序 1正序
    private lateinit var mAdapter: LikeCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_cards)
        initView()
        initToolBar("点赞")
        initLoadSir()
        mPresenter.getLikeCard(mSortType,mPage,mPageSize)
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this)
    }

    override fun createPresenter(): LikeCardsPresenter {
        return LikeCardsPresenter(this)
    }

    private fun getLikeCard() {
        mPresenter.getLikeCard(mSortType, mPage, Constants.PAGE_SIZE)
    }

    override fun getDataSuccess(list: List<LikeCardsBean>) {
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
        mPresenter.getLikeCard(mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as SearchCardBean
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initView() {
        mAdapter = LikeCardsAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_like_card)
        rv_like_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_like_card.adapter = mAdapter
        mAdapter.setEmptyView(R.layout.layout_no_data)

        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_card)
        iv_toolbar_right.setOnClickListener {
            if (mSortType == 1) {
                mSortType = 0
                iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_list)
            } else if (mSortType == 0) {
                mSortType = 1
                iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_card)
            }
            mPage = 0
            getLikeCard() }
    }
}
