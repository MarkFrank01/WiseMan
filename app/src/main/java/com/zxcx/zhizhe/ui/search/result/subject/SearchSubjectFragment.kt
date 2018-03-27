package com.zxcx.zhizhe.ui.search.result.subject

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_user.*

class SearchSubjectFragment : MvpFragment<SearchSubjectPresenter>(), SearchSubjectContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SubjectOnClickListener{

    var mPage = 0
    var mPageSize = Constants.PAGE_SIZE
    lateinit var mSearchUserAdapter : SearchSubjectAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchSubject(mKeyword,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun createPresenter(): SearchSubjectPresenter {
        return SearchSubjectPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchSubject(mKeyword,mPage,mPageSize)
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
        mSearchUserAdapter = SearchSubjectAdapter(ArrayList(),this)
        mSearchUserAdapter.setLoadMoreView(CustomLoadMoreView())
        mSearchUserAdapter.setOnLoadMoreListener(this,rv_search_result_user)
        rv_search_result_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_search_result_user.adapter = mSearchUserAdapter
        rv_search_result_user.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无搜索用户","换个关键词试试",null,null)
        mSearchUserAdapter.emptyView = emptyView
    }

    override fun getDataSuccess(list: List<SubjectBean>) {
        mSearchUserAdapter.mKeyword = mKeyword
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
        mPresenter.searchSubject(mKeyword,mPage,mPageSize)
    }
}