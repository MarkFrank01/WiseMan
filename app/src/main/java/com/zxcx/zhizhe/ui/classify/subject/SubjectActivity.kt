package com.zxcx.zhizhe.ui.classify.subject

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_user.*

class SubjectActivity : MvpActivity<SubjectPresenter>(), SubjectContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SubjectOnClickListener {

    var mPage = 0
    var mId = 0
    var mPageSize = Constants.PAGE_SIZE
    lateinit var mSearchUserAdapter : SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        initRecyclerView()
        mPresenter?.getSubject(mId,mPage,mPageSize)
    }

    override fun createPresenter(): SubjectPresenter {
        return SubjectPresenter(this)
    }

    override fun cardOnClick(bean: CardBean) {
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("author", bean.author)
        mActivity.startActivity(intent)
    }

    override fun subjectOnClick(bean: SubjectBean) {
        val intent = Intent(mActivity, CardBagActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        mActivity.startActivity(intent)
    }

    private fun initRecyclerView() {
        mSearchUserAdapter = SubjectAdapter(ArrayList(),this)
        mSearchUserAdapter.setLoadMoreView(CustomLoadMoreView())
        mSearchUserAdapter.setOnLoadMoreListener(this,rv_search_result_user)
        rv_search_result_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_search_result_user.adapter = mSearchUserAdapter
        rv_search_result_user.addItemDecoration(HomeCardItemDecoration())
        //todo 修改占位图
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无搜索用户",R.drawable.no_banner)
        mSearchUserAdapter.emptyView = emptyView
    }

    override fun getDataSuccess(list: List<SubjectBean>) {
        if (mPage == 0) {
            mSearchUserAdapter.setNewData(list)
        } else {
            mSearchUserAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mSearchUserAdapter.loadMoreEnd(false)
        } else {
            mSearchUserAdapter.loadMoreComplete()
            mSearchUserAdapter.setEnableLoadMore(false)
            mSearchUserAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter?.getSubject(mId,mPage,mPageSize)
    }
}
