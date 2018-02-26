package com.zxcx.zhizhe.ui.my.note.freedomNote

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitNoteReviewEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean
import com.zxcx.zhizhe.ui.my.note.noteDetails.NoteDetailsActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_freedom_note.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FreedomNoteFragment : RefreshMvpFragment<FreedomNotePresenter>(), FreedomNoteContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: FreedomNoteAdapter

    var mSortType = 0//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter?.getFreedomNote(mSortType, mPage, mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_freedom_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initRecyclerView()
        mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun createPresenter(): FreedomNotePresenter {
        return FreedomNotePresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CommitNoteReviewEvent) {
        val bean = NoteBean(event.noteId,null,null)
        mAdapter.remove(mAdapter.data.indexOf(bean))
    }

    override fun getDataSuccess(list: List<NoteBean>) {
        mRefreshLayout.finishRefresh()
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

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getFreedomNote(mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as NoteBean
        val intent = Intent(mActivity, NoteDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("noteType", Constants.NOTE_TYPE_FREEDOM)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = FreedomNoteAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_freedom_note)
        rv_freedom_note.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_freedom_note.adapter = mAdapter
        rv_freedom_note.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity,"好记性不如烂笔头","点击创作开始记录",R.color.button_blue,View.OnClickListener {
            if (ZhiZheUtils.isWriter(mActivity)) {
                val intent = Intent(mActivity, NewCreationTitleActivity::class.java)
                startActivity(intent)
            }
        })
        mAdapter.emptyView = emptyView
    }
}