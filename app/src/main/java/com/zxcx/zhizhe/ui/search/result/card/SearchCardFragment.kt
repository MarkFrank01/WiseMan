package com.zxcx.zhizhe.ui.search.result.card

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchCardFragment : MvpFragment<SearchCardPresenter>(), SearchCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    var mPage = 0
    var cardType = 0 //卡片类型 0卡片 1为长文
    private lateinit var mAdapter : SearchCardAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchCard(mKeyword,cardType,mPage)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchCard(mKeyword,cardType,mPage)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun createPresenter(): SearchCardPresenter {
        return SearchCardPresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListPositionEvent) {
        if (this::class.java.name == event.sourceName) {
            if (event.currentPosition == mAdapter.data.size-1) {
                mPresenter?.searchCard(mKeyword,cardType,mPage)
            }
            rv_search_result.scrollToPosition(event.currentPosition)
        }
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
        mPresenter.searchCard(mKeyword,cardType,mPage)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
        val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair<View, String>(cardImg, cardImg.transitionName),
                Pair<View, String>(cardTitle, cardTitle.transitionName)).toBundle()
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("list", mAdapter.data as ArrayList)
        intent.putExtra("currentPosition", position)
        intent.putExtra("sourceName",this::class.java.name)
        mActivity.startActivity(intent, bundle)
    }

    private fun initRecyclerView() {
        mAdapter = SearchCardAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_search_result)
        rv_search_result.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_search_result.adapter = mAdapter
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity,"暂无搜索卡片","换个关键词试试",R.drawable.search_no_data,View.OnClickListener {

        })
        mAdapter.emptyView = emptyView
    }
}