package com.zxcx.zhizhe.ui.classify.subject

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean
import com.zxcx.zhizhe.ui.search.result.subject.SubjectOnClickListener
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_subject.*

class SubjectActivity : MvpActivity<SubjectPresenter>(), SubjectContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SubjectOnClickListener {

    var mPage = 0
    var mId = 0
    var mPageSize = Constants.PAGE_SIZE
    var name = ""
    lateinit var mAdapter : SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        initData()
        initRecyclerView()
        mPresenter?.getSubject(mId,mPage,mPageSize)
    }

    private fun initData() {
        mId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
    }

    override fun createPresenter(): SubjectPresenter {
        return SubjectPresenter(this)
    }

    override fun getDataSuccess(list: List<SubjectBean>) {
        if (mPage == 0) {
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

    override fun onLoadMoreRequested() {
        mPresenter?.getSubject(mId,mPage,mPageSize)
    }

    private fun initRecyclerView() {
        mAdapter = SubjectAdapter(ArrayList(),this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_subject)
        rv_subject.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_subject.adapter = mAdapter
        rv_subject.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无搜索用户",R.drawable.no_data)
        mAdapter.emptyView = emptyView
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = name
        mAdapter.addHeaderView(header)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun cardOnClick(bean: CardBean) {
        startActivity(CardDetailsActivity::class.java,{
            it.putExtra("id", bean.id)
            it.putExtra("name", bean.name)
            it.putExtra("imageUrl", bean.imageUrl)
            it.putExtra("date", DateTimeUtils.getDateString(bean.date))
            it.putExtra("author", bean.author)
        })
    }

    override fun subjectOnClick(bean: SubjectBean) {
        startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.id)
            it.putExtra("name", bean.name)
            it.putExtra("isSubject",true)
        })
    }
}
