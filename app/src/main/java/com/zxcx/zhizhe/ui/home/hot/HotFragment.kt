package com.zxcx.zhizhe.ui.home.hot

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.classify.subject.SubjectCardActivity
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean
import com.zxcx.zhizhe.ui.search.result.subject.SubjectOnClickListener
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class HotFragment : RefreshMvpFragment<HotPresenter>(), HotContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, SubjectOnClickListener {

    private lateinit var mAdapter: HotCardAdapter
    private var mPage = 0
    private var mHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_hot, container, false)
        mRefreshLayout = root.findViewById(R.id.refresh_layout)
        val loadSir = LoadSir.Builder()
                .addCallback(HomeLoadingCallback())
                .addCallback(LoginTimeoutCallback())
                .addCallback(HomeNetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback::class.java)
                .build()
        loadService = loadSir.register(root) { v ->
            loadService.showCallback(HomeLoadingCallback::class.java)
            getHotCard()
        }
        return loadService.loadLayout
    }

    override fun createPresenter(): HotPresenter {
        return HotPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initRecyclerView()

        getHotCard()
        mHidden = false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.setUserVisibleHint(hidden)
        mHidden = hidden
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun clearLeaks() {
        loadService = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomeClickRefreshEvent) {
        if (!mHidden) {
            rv_hot_card.scrollToPosition(0)
            mRefreshLayout.autoRefresh()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 0
        getHotCard()
    }

    override fun onLoadMoreRequested() {
        getHotCard()
    }

    override fun getDataSuccess(list: List<HotBean>) {
        //loadService.showSuccess()的调用必须在PtrFrameLayout.refreshComplete()之前，因为loadService的调用会使得界面重新加载，这将导致PtrFrameLayout移除
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            mAdapter.setNewData(list)
            rv_hot_card.scrollToPosition(0)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size == 0) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
        if (mAdapter.data.size == 0) {
            //占空图
        }
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            loadService.showCallback(HomeNetworkErrorCallback::class.java)
        }
    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = HotCardAdapter(ArrayList(), this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_hot_card)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        rv_hot_card.layoutManager = layoutManager
        rv_hot_card.adapter = mAdapter
        rv_hot_card.addItemDecoration(HomeCardItemDecoration())
    }

    private fun getHotCard() {
        mPresenter.getHotCard(mPage)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val hotBean = adapter.data[position] as HotBean
        if (hotBean.itemType == HotBean.TYPE_CARD) {
            val bean = hotBean.cardBean
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
                    Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
                    Pair.create(view.findViewById(R.id.tv_item_card_card_bag), "cardBag")).toBundle()
            val intent = Intent(mActivity, CardDetailsActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            intent.putExtra("imageUrl", bean.imageUrl)
            intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
            intent.putExtra("author", bean.author)
            mActivity.startActivity(intent, bundle)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardBagName)
        })
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
        val intent = Intent(mActivity, SubjectCardActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        mActivity.startActivity(intent)
    }
}