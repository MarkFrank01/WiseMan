package com.zxcx.zhizhe.ui.article.attention

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
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.*
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.SelectAttentionCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean
import com.zxcx.zhizhe.ui.classify.subject.SubjectArticleActivity
import com.zxcx.zhizhe.ui.home.hot.ArticleAndSubjectAdapter
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_attention.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class AttentionArticleFragment : RefreshMvpFragment<AttentionArticlePresenter>(), AttentionArticleContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, SubjectOnClickListener {

	private lateinit var mAdapter: ArticleAndSubjectAdapter
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

	override fun createPresenter(): AttentionArticlePresenter {
		return AttentionArticlePresenter(this)
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

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		onRefresh()
	}

	private fun onRefresh() {
		page = 0
		getAttentionCard()
	}

	override fun onLoadMoreRequested() {
		getAttentionCard()
	}

	override fun getDataSuccess(list: List<ArticleAndSubjectBean>) {
		loadService.showSuccess()
		if (list.isEmpty() && page == 0) {
			mPresenter.getClassify()
			return
		}
		if (page == 0) {
			mRefreshLayout.finishRefresh()
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
		val selectAttentionCallback = SelectAttentionCallback(list)
		loadService.loadLayout.setupCallback(selectAttentionCallback)
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
		mAdapter = ArticleAndSubjectAdapter(ArrayList(), this)
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_attention_card)
		mAdapter.onItemClickListener = this
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
		val bean = adapter.data[position] as ArticleAndSubjectBean
		if (bean.itemType == ArticleAndSubjectBean.TYPE_CARD) {
			val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
			val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
			val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
			val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
			val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
					Pair.create(cardImg, cardImg.transitionName),
					Pair.create(cardTitle, cardTitle.transitionName),
					Pair.create(cardCategory, cardCategory.transitionName),
					Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
			val intent = Intent(mActivity, CardDetailsActivity::class.java)
			intent.putExtra("list", mAdapter.data as ArrayList)
			intent.putExtra("currentPosition", position)
			intent.putExtra("sourceName", this::class.java.name)
			mActivity.startActivity(intent, bundle)
		}
	}

	override fun articleOnClick(bean: CardBean) {
		val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		intent.putExtra("category", bean.cardCategoryName)
		intent.putExtra("label", bean.cardLabelName)
		intent.putExtra("imageUrl", bean.imageUrl)
		intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
		mActivity.startActivity(intent)
	}

	override fun subjectOnClick(bean: SubjectBean) {
		val intent = Intent(mActivity, SubjectArticleActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		mActivity.startActivity(intent)
	}
}