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
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.my.note.freedomNote.FreedomNoteAdapter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_freedom_note.*

class FreedomNoteFragment : MvpFragment<FreedomNotePresenter>(), FreedomNoteContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: FreedomNoteAdapter

    var mSortType = 1//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_freedom_note, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
    }

    override fun createPresenter(): FreedomNotePresenter {
        return FreedomNotePresenter(this)
    }

    override fun getDataSuccess(list: List<FreedomNoteBean>) {
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
        mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as FreedomNoteBean
        val intent = Intent(mActivity,CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = FreedomNoteAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_freedom_note)
        rv_freedom_note.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_freedom_note.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无自由笔记","点击创作开始记录",R.color.button_blue,View.OnClickListener {
            val intent = Intent(mActivity,NewCreationTitleActivity::class.java)
            startActivity(intent)
        })
        mAdapter.emptyView = emptyView
    }
}