package com.zxcx.zhizhe.ui.my.note

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.my.likeCards.SwipeMenuClickListener
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_note.*

/**
 * Created by anm on 2017/12/14.
 */
class NoteActivity : MvpActivity<NotePresenter>(), NoteContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeMenuClickListener {
    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_cards)
        initView()
        initLoadSir()
        mPresenter.getNoteList(mPage,mPageSize)
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(rv_note, this)
    }

    override fun createPresenter(): NotePresenter {
        return NotePresenter(this)
    }

    override fun getDataSuccess(list: List<NoteBean>) {
        if (mPage == 0) {
            loadService.showSuccess()
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

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
    }

    override fun postSuccess() {
        //删除成功
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getNoteList(mPage,mPageSize)
    }

    override fun onContentClick(position: Int) {
        val bean = mAdapter.data[position] as NoteBean
        //todo 笔记列表点击
    }

    override fun onDeleteClick(position: Int) {
        mPresenter.deleteNote(mAdapter.data[position].id)
    }

    private fun initView() {
        mAdapter = NoteAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_note)
        mAdapter.mListener = this
        rv_note.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_note.adapter = mAdapter
        rv_note.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity,"涨知识 点点赞", "快去给你喜欢的卡片点赞吧~", null, null)
        mAdapter.emptyView = emptyView
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = "点赞"
        mAdapter.addHeaderView(header)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }
}