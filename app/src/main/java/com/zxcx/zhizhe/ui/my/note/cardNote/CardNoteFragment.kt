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
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_card_note.*

class CardNoteFragment : MvpFragment<CardNotePresenter>(), CardNoteContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: CardNoteAdapter

    var mSortType = 1//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter.getCardNote(mSortType,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_note, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter.getCardNote(mSortType,mPage,mPageSize)
    }

    override fun createPresenter(): CardNotePresenter {
        return CardNotePresenter(this)
    }

    override fun getDataSuccess(list: List<CardNoteBean>) {
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
        mPresenter.getCardNote(mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardNoteBean
        val intent = Intent(mActivity,CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = CardNoteAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_card_note)
        rv_card_note.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_card_note.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无卡片笔记","卡片笔记可以从卡片中记录",null,null)
        mAdapter.emptyView = emptyView
    }
}