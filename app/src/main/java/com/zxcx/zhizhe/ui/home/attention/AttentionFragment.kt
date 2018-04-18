package com.zxcx.zhizhe.ui.home.attention

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
import com.zxcx.zhizhe.event.*
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.classify.subject.SubjectCardActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.home.hot.HotBean
import com.zxcx.zhizhe.ui.home.hot.HotCardAdapter
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean
import com.zxcx.zhizhe.ui.search.result.subject.SubjectOnClickListener
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class AttentionFragment : RefreshMvpFragment<AttentionPresenter>(), AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, SubjectOnClickListener {

    private lateinit var mAdapter: HotCardAdapter
    private var page = 0
    private var mHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_attention, container, false)

        mRefreshLayout = root.findViewById(R.id.refresh_layout)

        val loadSir = LoadSir.Builder()
                .addCallback(AttentionNeedLoginCallback())
                .addCallback(HomeLoadingCallback())
                .addCallback(HomeNetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback::class.java)
                .build()
        loadService = loadSir.register(root) { v ->
            if (checkLogin()) {
                loadService.showCallback(HomeLoadingCallback::class.java)
                onRefresh()
            }
        }
        return loadService.loadLayout
    }

    override fun createPresenter(): AttentionPresenter {
        return AttentionPresenter(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        mHidden = hidden
        super.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initView()
        mHidden = false
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LoginEvent) {
        loadService.showCallback(HomeLoadingCallback::class.java)
        onRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        loadService.showCallback(AttentionNeedLoginCallback::class.java)
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SelectAttentionEvent) {
        loadService.showCallback(HomeLoadingCallback::class.java)
        onRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FollowUserRefreshEvent) {
        loadService.showCallback(HomeLoadingCallback::class.java)
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    private fun onRefresh() {
        page = 0
        getAttentionCard()
    }

    override fun onLoadMoreRequested() {
        getAttentionCard()
    }

    override fun getDataSuccess(list: List<HotBean>) {
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (page == 0) {
            mAdapter.setNewData(list)
            rv_hot_card.scrollToPosition(0)
        } else {
            mAdapter.addData(list)
        }
        page++
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
        if (page == 0) {
            loadService.showCallback(HomeNetworkErrorCallback::class.java)
        }
    }

    override fun startLogin() {
        ZhiZheUtils.logout()
        toastShow(R.string.login_timeout)
        startActivity(Intent(mActivity, LoginActivity::class.java))
        if (loadService != null) {
            loadService.showCallback(AttentionNeedLoginCallback::class.java)
        }
    }

    private fun initView() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = HotCardAdapter(ArrayList(), this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_hot_card)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity, "暂无关注", "看看你喜欢什么", R.drawable.no_data, View.OnClickListener {
            mActivity.startActivity(SelectAttentionActivity::class.java,{})
        })

        mAdapter.emptyView = emptyView
        rv_hot_card.layoutManager = layoutManager
        rv_hot_card.adapter = mAdapter
        rv_hot_card.addItemDecoration(HomeCardItemDecoration())
        if (checkLogin()) {
            onRefresh()
        } else {
            loadService.showCallback(AttentionNeedLoginCallback::class.java)
        }
    }

    private fun getAttentionCard() {
        mPresenter.getAttentionCard(page, Constants.PAGE_SIZE)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = (adapter.data[position] as HotBean).cardBean
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
        intent.putExtra("isSubject", true)
        mActivity.startActivity(intent)
    }
}