package com.zxcx.zhizhe.ui.search.result.card

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
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_card.*

class SearchCardFragment : MvpFragment<SearchCardPresenter>(), SearchCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mSearchCardAdapter : SearchCardAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchCard(mKeyword,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search_card, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchCard(mKeyword,mPage,mPageSize)
    }

    override fun createPresenter(): SearchCardPresenter {
        return SearchCardPresenter(this)
    }

    override fun getDataSuccess(list: List<SearchCardBean>) {
        mSearchCardAdapter.mKeyword = mKeyword
        if (mPage == 0) {
            mSearchCardAdapter.setNewData(list)
        } else {
            mSearchCardAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mSearchCardAdapter.loadMoreEnd(false)
        } else {
            mSearchCardAdapter.loadMoreComplete()
            mSearchCardAdapter.setEnableLoadMore(false)
            mSearchCardAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.searchCard(mKeyword,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as SearchCardBean
        val intent = Intent(mActivity,CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mSearchCardAdapter = SearchCardAdapter(ArrayList())
        mSearchCardAdapter.onItemClickListener = this
        mSearchCardAdapter.setLoadMoreView(CustomLoadMoreView())
        mSearchCardAdapter.setOnLoadMoreListener(this,rv_search_result_card)
        rv_search_result_card.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_search_result_card.adapter = mSearchCardAdapter
        rv_search_result_card.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无搜索结果","换个关键词试试",null,null)
        mSearchCardAdapter.emptyView = emptyView
    }
}