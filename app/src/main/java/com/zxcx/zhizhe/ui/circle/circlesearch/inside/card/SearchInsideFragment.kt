package com.zxcx.zhizhe.ui.circle.circlesearch.inside.card

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.UpdateCardListEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideFragment :MvpFragment<SearchInsidePresenter>(), SearchInsideContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    //circleId
    var circleId = 0

    var mPage = 0
    var cardType = 0 //卡片类型 0卡片 1为长文
    private lateinit var mAdapter: SearchInsideAdapter


    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchCard(mPage,Constants.PAGE_SIZE,circleId,cardType,mKeyword)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchCard(mPage,Constants.PAGE_SIZE,circleId,cardType,mKeyword)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun createPresenter(): SearchInsidePresenter {
        return SearchInsidePresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListPositionEvent) {
        if (this::class.java.name == event.sourceName) {
            if (event.currentPosition == mAdapter.data.size - 1) {
                mPresenter?.searchCard(mPage,Constants.PAGE_SIZE,circleId,cardType,mKeyword)
            }
            rv_search_result.scrollToPosition(event.currentPosition)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListEvent) {
        mAdapter.data[event.currentPosition] = event.cardBean
        mAdapter.notifyItemChanged(event.currentPosition)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        mAdapter.mKeyword = mKeyword
        if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
            val event = AddCardDetailsListEvent(this::class.java.name, list as ArrayList<CardBean>?)
            EventBus.getDefault().post(event)
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
        mPresenter?.searchCard(mPage,Constants.PAGE_SIZE,circleId,cardType,mKeyword)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mActivity.startActivity(CardDetailsActivity::class.java) {
            it.putExtra("list", mAdapter.data as ArrayList)
            it.putExtra("currentPosition", position)
            it.putExtra("sourceName", this::class.java.name)
        }
    }

    private fun initRecyclerView() {
        mAdapter = SearchInsideAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_search_result)
        rv_search_result.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_search_result.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容，换个关键词试试", R.drawable.iv_need_login)
        mAdapter.emptyView = emptyView
    }
}