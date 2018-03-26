package com.zxcx.zhizhe.ui.card.cardBag

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_card_bag.*
import java.util.*

class CardBagActivity : RefreshMvpActivity<CardBagPresenter>(), CardBagContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    private lateinit var mCardBagCardAdapter: CardBagCardAdapter
    private var isCard = true
    private var mId: Int = 0
    private var page = 0
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_bag)
        ButterKnife.bind(this)
        initData()
        initView()
        onRefresh()

        initLoadSir()
    }

    override fun createPresenter(): CardBagPresenter {
        return CardBagPresenter(this)
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

    override fun onReload(v: View) {
        loadService.showCallback(LoadingCallback::class.java)
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun onRefresh() {
        page = 0
        getCardBagCardList()
    }

    override fun onLoadMoreRequested() {
        getCardBagCardList()
    }

    override fun getDataSuccess(list: List<CardBagBean>) {
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (page == 0) {
            mCardBagCardAdapter.setNewData(list)
        } else {
            mCardBagCardAdapter.addData(list)
        }
        page++
        if (list.size < Constants.PAGE_SIZE) {
            mCardBagCardAdapter.loadMoreEnd(false)
        } else {
            mCardBagCardAdapter.loadMoreComplete()
            mCardBagCardAdapter.setEnableLoadMore(false)
            mCardBagCardAdapter.setEnableLoadMore(true)
        }
    }

    override fun toastFail(msg: String) {
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
        super.toastFail(msg)
    }

    private fun getCardBagCardList() {
        mPresenter.getCardBagCardList(mId, page, Constants.PAGE_SIZE)
    }

    private fun initData() {
        mId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
        tv_header_title.text = name
    }

    private fun initView() {
        mCardBagCardAdapter = CardBagCardAdapter(ArrayList())
        mCardBagCardAdapter.setLoadMoreView(CustomLoadMoreView())
        mCardBagCardAdapter.setOnLoadMoreListener(this, rv_card_bag_card)
        mCardBagCardAdapter.onItemClickListener = CardItemClickListener(this)
        val view = EmptyView.getEmptyView(mActivity, "暂无相关内容", "期待你在此分享创作", null, null)
        mCardBagCardAdapter.emptyView = view

        rv_card_bag_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_card_bag_card.adapter = mCardBagCardAdapter
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }

    internal inner class CardItemClickListener(private val mContext: Activity) : BaseQuickAdapter.OnItemClickListener {

        override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
            val bean = adapter.data[position] as CardBagBean
            val intent = Intent(mContext, CardDetailsActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            intent.putExtra("imageUrl", bean.imageUrl)
            intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
            intent.putExtra("author", bean.author)
            if (isCard) {
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
                        Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
                        Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
                        Pair.create(view.findViewById(R.id.tv_item_card_card_bag), "cardBag")).toBundle()
                mContext.startActivity(intent, bundle)
            } else {
                mContext.startActivity(intent)
            }
        }
    }

}
