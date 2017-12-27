package com.zxcx.zhizhe.ui.search.result.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.home.rank.UserRankBean
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_user.*

class SearchUserFragment : MvpFragment<SearchUserPresenter>(), SearchUserContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    var mPage = 0
    var mPageSize = Constants.PAGE_SIZE
    lateinit var mSearchUserAdapter : SearchUserAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchUser(mKeyword,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun createPresenter(): SearchUserPresenter {
        return SearchUserPresenter(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchUser(mKeyword,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as UserRankBean
        val intent = Intent(mActivity, OtherUserActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mSearchUserAdapter = SearchUserAdapter(ArrayList())
        mSearchUserAdapter.setLoadMoreView(CustomLoadMoreView())
        mSearchUserAdapter.setOnLoadMoreListener(this,rv_search_result_user)
        rv_search_result_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_search_result_user.adapter = mSearchUserAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无搜索结果","换个关键词试试",null,null)
        mSearchUserAdapter.emptyView = emptyView
    }

    override fun getDataSuccess(list: List<SearchUserBean>) {
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
        mPresenter.searchUser(mKeyword,mPage,mPageSize)
    }
}