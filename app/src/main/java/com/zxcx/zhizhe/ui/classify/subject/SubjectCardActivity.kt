package com.zxcx.zhizhe.ui.classify.subject

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_subject.*

class SubjectCardActivity : MvpActivity<SubjectCardPresenter>(), SubjectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    var mPage = 0
    var mId = 0
    var mPageSize = Constants.PAGE_SIZE
    var name = ""
    lateinit var mCardAdapter : SubjectCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        initData()
        initRecyclerView()
        mPresenter?.getSubjectCardList(mId,mPage,mPageSize)
    }

    private fun initData() {
        mId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
    }

    override fun createPresenter(): SubjectCardPresenter {
        return SubjectCardPresenter(this)
    }

    override fun getDataSuccess(list: List<CardBean>) {
        if (mPage == 0) {
            mCardAdapter.setNewData(list)
        } else {
            mCardAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mCardAdapter.loadMoreEnd(false)
        } else {
            mCardAdapter.loadMoreComplete()
            mCardAdapter.setEnableLoadMore(false)
            mCardAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter?.getSubjectCardList(mId,mPage,mPageSize)
    }

    private fun initRecyclerView() {
        mCardAdapter = SubjectCardAdapter(ArrayList())
        mCardAdapter.setLoadMoreView(CustomLoadMoreView())
        mCardAdapter.onItemClickListener = this
        mCardAdapter.onItemChildClickListener = this
        mCardAdapter.setOnLoadMoreListener(this,rv_subject)
        rv_subject.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_subject.adapter = mCardAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无内容",R.drawable.no_data)
        mCardAdapter.emptyView = emptyView
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = name
        mCardAdapter.addHeaderView(header)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
                Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
                Pair.create(view.findViewById(R.id.tv_item_card_card_bag), "cardBag")).toBundle()
        val intent = Intent(this, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("author", bean.author)
        startActivity(intent, bundle)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardBagName)
        })
    }
}
