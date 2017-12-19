package com.zxcx.zhizhe.ui.search.result.card

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_creation.*

class CreationInReviewFragment : RefreshMvpFragment<CreationPresenter>(), CreationContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    private var mPage = 0
    private val mPassType = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: CreationAdapter

    var mSortType = 1//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter.getCreation(mPassType,mSortType,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_creation, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRefreshLayout = refresh_layout
        initRecyclerView()
        mPresenter.getCreation(mPassType,mSortType,mPage,mPageSize)
    }

    override fun createPresenter(): CreationPresenter {
        return CreationPresenter(this)
    }

    override fun getDataSuccess(list: List<CreationBean>) {
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

    override fun onRefreshBegin(frame: PtrFrameLayout?) {
        mPage = 0
        mPresenter.getCreation(mPassType,mSortType,mPage,mPageSize)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getCreation(mPassType,mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CreationBean
        val intent = Intent(mActivity,CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = CreationAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_creation)
        rv_creation.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_creation.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无审核内容","和大家分享你的知识吧",R.color.button_blue,View.OnClickListener {
            val intent = Intent(mActivity, NewCreationTitleActivity::class.java)
            startActivity(intent)
        })
        mAdapter.emptyView = emptyView
    }
}