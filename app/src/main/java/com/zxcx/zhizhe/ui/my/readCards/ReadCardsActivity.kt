package com.zxcx.zhizhe.ui.my.readCards

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.my.likeCards.SwipeMenuClickListener
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_read_cards.*

class ReadCardsActivity : MvpActivity<ReadCardsPresenter>(), ReadCardsContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeMenuClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: ReadCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_cards)
        initView()
        initLoadSir()
        mPresenter.getReadCard(mPage,mPageSize)
        mPresenter.getEmptyRecommendCard()
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(rv_read_card, this)
    }

    override fun createPresenter(): ReadCardsPresenter {
        return ReadCardsPresenter(this)
    }

    override fun getEmptyRecommendCardSuccess(bean: CardBean) {
        val emptyView = EmptyView.getEmptyViewAndCard(mActivity,"涨知识 点点赞", R.drawable.no_data, bean)
        mAdapter.emptyView = emptyView
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

    override fun postSuccess() {
        //删除成功
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getReadCard(mPage,mPageSize)
    }

    override fun onContentClick(position: Int) {
        val bean = mAdapter.data[position] as ReadCardsBean
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("author", bean.author)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.iv_item_card_icon), "cardImage"),
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.tv_item_card_title), "cardTitle"),
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.tv_item_card_card_bag), "cardBag")).toBundle()
        mActivity.startActivity(intent, bundle)
    }

    override fun onDeleteClick(position: Int) {
        mPresenter.deleteReadCard(mAdapter.data[position].realId,mAdapter.data[position].id)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardBagName)
        })
    }

    private fun initView() {
        mAdapter = ReadCardsAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_read_card)
        mAdapter.mListener = this
        rv_read_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_read_card.adapter = mAdapter
        rv_read_card.addItemDecoration(HomeCardItemDecoration())
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = "阅读"
        mAdapter.addHeaderView(header)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }
}