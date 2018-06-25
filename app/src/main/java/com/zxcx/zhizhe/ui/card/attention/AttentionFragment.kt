package com.zxcx.zhizhe.ui.card.attention

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.*
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.SelectAttentionCallback
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.card.hot.CardAdapter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean
import com.zxcx.zhizhe.ui.classify.subject.SubjectCardActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.search.result.SubjectBean
import com.zxcx.zhizhe.ui.search.result.SubjectOnClickListener
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_attention.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class AttentionFragment : MvpFragment<AttentionPresenter>(), AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, SubjectOnClickListener {

    private lateinit var mAdapter: CardAdapter
    private var page = 0
    private var mHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_attention, container, false)

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
            rv_attention_card.scrollToPosition(0)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ChangeAttentionEvent) {
        val idList = mutableListOf<Int>()
        event.list.forEach {
            idList.add(it.id)
        }
        mPresenter.changeAttentionList(idList)
    }

    private fun onRefresh() {
        page = 0
        getAttentionCard()
    }

    override fun onLoadMoreRequested() {
        getAttentionCard()
    }

    override fun getDataSuccess(list: List<CardBean>) {
        if (list.isEmpty() && page == 0){
            mPresenter.getClassify()
        }
        loadService.showSuccess()
        if (page == 0) {
            mAdapter.setNewData(list)
            rv_attention_card.scrollToPosition(0)
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

    override fun postSuccess() {
        onRefresh()
    }

    override fun postFail(msg: String) {
        toastFail(msg)
    }

    override fun getClassifySuccess(list: List<ClassifyBean>) {
        loadService.loadLayout.addCallback(SelectAttentionCallback(list))
        loadService.showCallback(SelectAttentionCallback::class.java)
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
        mAdapter = CardAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_attention_card)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity, "暂无内容", "选择兴趣内容", R.drawable.no_data, View.OnClickListener {
            mActivity.startActivity(SelectAttentionActivity::class.java,{})
        })

        mAdapter.emptyView = emptyView
        rv_attention_card.layoutManager = layoutManager
        rv_attention_card.adapter = mAdapter
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
            onRefresh()
        } else {
            loadService.showCallback(AttentionNeedLoginCallback::class.java)
        }
    }

    private fun getAttentionCard() {
        mPresenter.getAttentionCard(page, Constants.PAGE_SIZE)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        /*val bean = (adapter.data[position] as CardBean).cardBean
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
                Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
                Pair.create(view.findViewById(R.id.tv_item_card_category), "cardBag")).toBundle()
        val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("authorName", bean.authorName)
        mActivity.startActivity(intent, bundle)*/
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        mActivity.startActivity(CardBagActivity::class.java) {
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardCategoryName)
        }
    }

    override fun cardOnClick(bean: CardBean) {
        val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("authorName", bean.authorName)
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