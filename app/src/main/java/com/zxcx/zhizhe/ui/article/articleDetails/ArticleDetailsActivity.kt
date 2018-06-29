package com.zxcx.zhizhe.ui.article.articleDetails

import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.*
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.retrofit.APIService
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GoodView
import kotlinx.android.synthetic.main.activity_article_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ArticleDetailsActivity : MvpActivity<ArticleDetailsPresenter>(), ArticleDetailsContract.View {

	private var mWebView: WebView? = null
	private var cardId: Int = 0
	private var cardBagId: Int = 0
	private var subjectId: Int = 0
	private var mUserId: Int = 0
	private var mAuthorId: Int = 0
	private var mCardType: Int = 0
	private var name: String? = null
	private var cardBagName: String? = null
	private var subjectName: String? = null
	private var imageUrl: String? = null
	private var collectStatus = false
	private var isUnCollect = false
	private var likeStatus = false
	private var isUnLike = false
	private var date: String? = null
	private var category: String? = null
	private var label: String? = null
	private var author: String? = null
	private var mActionMode: ActionMode? = null
	private var mUrl: String? = null
	private lateinit var startDate: Date
	private var loadService2: LoadService<*>? = null
	private var loadSir2: LoadSir? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_article_details)
		ButterKnife.bind(this)
		EventBus.getDefault().register(this)

		initData()
		initView()

		mPresenter.getCardDetails(cardId)
		startDate = Date()
	}

	override fun initStatusBar() {

	}

	override fun onBackPressed() {
		super.onBackPressed()
		if (isUnCollect) {
			EventBus.getDefault().post(UnCollectEvent(cardId))
		}
		if (isUnLike) {
			EventBus.getDefault().post(UnLikeEvent(cardId))
		}
	}

	override fun onResume() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		super.onResume()
	}

	override fun onDestroy() {
		val endDate = Date()
		if (endDate.time - startDate.time > 30000 && mUserId != 0) {
			mPresenter.readArticle(cardId)
		}
		if (mWebView != null) {
			mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
			mWebView?.clearHistory()

			(mWebView?.parent as ViewGroup).removeView(mWebView)
			mWebView?.destroy()
			mWebView = null
		}
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun onActionModeStarted(mode: ActionMode) {
		if (mActionMode == null) {
			super.onActionModeStarted(mode)
			mActionMode = mode
			val menu = mode.menu
			menu.clear()
			val menuItemClickListener = MenuItemClickListener()
			menu.add(0, MENU_ITEM_NOTE, 0, "保存笔记").setOnMenuItemClickListener(menuItemClickListener)
			menu.add(0, MENU_ITEM_SHARE, 0, "图文分享").setOnMenuItemClickListener(menuItemClickListener)
		}
	}


	override fun onActionModeFinished(mode: ActionMode) {
		mActionMode = null
		mWebView?.clearFocus()//移除高亮显示,如果不移除在三星s6手机上会崩溃
		super.onActionModeFinished(mode)
	}

	override fun createPresenter(): ArticleDetailsPresenter {
		return ArticleDetailsPresenter(this)
	}

	override fun onReload(v: View) {
		mPresenter.getCardDetails(cardId)
		mWebView?.reload()
	}

	override fun getDataSuccess(bean: ArticleDetailsBean) {
		collectStatus = bean.isCollect
		likeStatus = bean.isLike
		postSuccess(bean)
		//进入时只有id的时候，在这里初始化界面
		name = bean.name
		imageUrl = bean.imageUrl
		date = DateTimeUtils.getDateString(bean.date)
		category = bean.categoryName
		label = bean.labelName
		tv_article_details_title.text = name
		tv_article_details_date.text = date
		tv_article_details_category.text = category
		tv_article_details_label.text = label
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_article_details)
		tv_article_details_author.text = bean.authorName
		tv_article_details_comment.text = bean.commentNum.toString()
		tv_article_details_collect.text = bean.collectNum.toString()
		tv_article_details_like.text = bean.likeNum.toString()
		cb_article_details_follow.isChecked = bean.isFollow
		cb_article_details_collect.isChecked = bean.isCollect
		cb_article_details_like.isChecked = bean.isLike
		val ad = bean.ad
		if (ad != null) {
			group_article_ad.visibility = View.VISIBLE
			ImageLoader.load(mActivity, ad.content, R.drawable.default_card, iv_article_details_ad)
			iv_article_details_ad.setOnClickListener {
				startActivity(WebViewActivity::class.java) {
					it.putExtra("title", ad.description)
					it.putExtra("url", ad.behavior)
				}
			}
		}
	}

	override fun likeSuccess(bean: ArticleDetailsBean) {
		toastShow("点赞成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.likeNum - 1).toString() + " +1")
		goodView.show(cb_article_details_like)
	}

	override fun collectSuccess(bean: ArticleDetailsBean) {
		toastShow("收藏成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.collectNum - 1).toString() + " +1")
		goodView.show(cb_article_details_collect)
	}

	override fun postSuccess(bean: ArticleDetailsBean) {
		isUnCollect = collectStatus != bean.isCollect && !bean.isCollect
		isUnLike = likeStatus != bean.isLike && !bean.isLike

		tv_article_details_title.text = name
		tv_article_details_date.text = date
		tv_article_details_category.text = category
		tv_article_details_label.text = label
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_article_details)
		tv_article_details_author.text = bean.authorName
		tv_article_details_comment.text = bean.commentNum.toString()
		tv_article_details_collect.text = bean.collectNum.toString()
		tv_article_details_like.text = bean.likeNum.toString()
		cb_article_details_follow.isChecked = bean.isFollow
		cb_article_details_collect.isChecked = bean.isCollect
		cb_article_details_like.isChecked = bean.isLike
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}

	override fun followSuccess() {
		if (cb_article_details_follow.isChecked) {
			//取消成功
			cb_article_details_follow.isChecked = false
		} else {
			//关注成功
			toastShow("关注成功")
			cb_article_details_follow.isChecked = true
		}
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnFollowConfirmEvent) {
		//取消关注
		mPresenter.setUserFollow(mAuthorId, 1)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: SaveCardNoteSuccessEvent) {
		//卡片笔记保存成功
		toastShow("保存成功")
	}

	override fun setListener() {
		iv_article_details_back.setOnClickListener {
			onBackPressed()
		}
		tv_article_details_label.setOnClickListener {
			//todo 标签页
		}
		tv_article_details_author.setOnClickListener {
			startActivity(OtherUserActivity::class.java) {
				it.putExtra("name", author)
				it.putExtra("id", mAuthorId)
			}
		}
		cb_article_details_follow.setOnClickListener {
			cb_article_details_follow.isChecked = !cb_article_details_follow.isChecked
			if (!checkLogin()) {
				return@setOnClickListener
			}
			if (mUserId == mAuthorId) {
				toastShow("无法关注自己")
				return@setOnClickListener
			}
			if (!cb_article_details_follow.isChecked) {
				//关注
				mPresenter.setUserFollow(mAuthorId, 0)
			} else {
				//取消关注弹窗
				val dialog = UnFollowConfirmDialog()
				val bundle = Bundle()
				bundle.putInt("userId", mAuthorId)
				dialog.arguments = bundle
				dialog.show(fragmentManager, "")
			}
		}
		iv_article_details_comment.setOnClickListener {
			//todo 评论区
		}
		cb_article_details_collect.setOnClickListener {
			//checkBox点击之后选中状态就已经更改了
			cb_article_details_collect.isChecked = !cb_article_details_collect.isChecked
			if (mUserId == mAuthorId) {
				toastShow("不能收藏自己哦")
				return@setOnClickListener
			}
			if (!cb_article_details_collect.isChecked) {
				if (checkLogin()) {
					mPresenter.addCollectCard(cardId)
				}
			} else {
				mPresenter.removeCollectCard(cardId)
			}
		}
		cb_article_details_like.setOnClickListener {
			//checkBox点击之后选中状态就已经更改了
			cb_article_details_like.isChecked = !cb_article_details_like.isChecked
			if (mUserId == mAuthorId) {
				toastShow("不能点赞自己哦")
				return@setOnClickListener
			}
			if (!cb_article_details_like.isChecked) {
				if (checkLogin()) {
					mPresenter.likeCard(cardId)
				}
			} else {
				mPresenter.removeLikeCard(cardId)
			}
		}
		iv_article_details_share.setOnClickListener {
			gotoHtmlShare()
		}
	}

	private fun initData() {
		cardId = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		imageUrl = intent.getStringExtra("imageUrl")
		date = intent.getStringExtra("date")
		category = intent.getStringExtra("category")
		label = intent.getStringExtra("label")
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
	}

	private fun initView() {
		if (!StringUtils.isEmpty(name))
			tv_article_details_title.text = name
		if (!StringUtils.isEmpty(date))
			tv_article_details_date.text = date
		if (!StringUtils.isEmpty(category))
			tv_article_details_category.text = category
		if (!StringUtils.isEmpty(label))
			tv_article_details_label.text = label
		if (!StringUtils.isEmpty(imageUrl))
			ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_article_details)
		initWebView()
		initLoadSir()
	}

	private fun initWebView() {
		//获取WebView，并将WebView高度设为WRAP_CONTENT
		mWebView = WebViewUtils.getWebView(this)
		val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		mWebView?.layoutParams = params

		mWebView?.webViewClient = object : WebViewClient() {

			var isError = false

			override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
				return true
			}

			override fun onPageFinished(view: WebView, url: String) {
				if (isError) {
					isError = false
					return
				}
				if (loadService != null) {
					loadService.showSuccess()
					loadService = null
				}
				if (loadService2 != null) {
					loadService2?.showSuccess()
				}
				group_article_bottom.visibility = View.VISIBLE
			}

			override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
				super.onReceivedError(view, request, error)
				isError = true
				loadService.showSuccess()
				if (loadService2 == null) {
					loadService2 = loadSir2?.register(sv_card_details, this@ArticleDetailsActivity)
				}
				loadService2?.showCallback(CardDetailsNetworkErrorCallback::class.java)
			}
		}
		mWebView?.isFocusable = false
		fl_article_details.addView(mWebView)
		val isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false)
		val fontSize = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue, 1)
		mUrl = if (isNight) {
			APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + cardId + "?fontSize=" + fontSize
		} else {
			APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + cardId + "?fontSize=" + fontSize

		}
		mWebView?.loadUrl(mUrl)
	}

	private fun initLoadSir() {
		val loadSir = LoadSir.Builder()
				.addCallback(CardDetailsLoadingCallback())
				.setDefaultCallback(CardDetailsLoadingCallback::class.java)
				.build()
		loadService = loadSir.register(mWebView, this)
		loadSir2 = LoadSir.Builder()
				.addCallback(CardDetailsNetworkErrorCallback())
				.build()
	}

	private fun gotoHtmlShare() {
		val shareCardDialog = ShareDialog()
		val bundle = Bundle()
		bundle.putString("title", getString(R.string.app_name))
		bundle.putString("text", name)
		bundle.putString("url", getString(R.string.base_url) + getString(R.string.card_share_url) + cardId.toString())
		bundle.putString("imageUrl", imageUrl)
		shareCardDialog.arguments = bundle
		shareCardDialog.show(fragmentManager, "")
	}

	private fun gotoImageShare(url: String?, content: String?) {
		val shareDialog = ShareCardDialog()
		val bundle = Bundle()
		bundle.putString("name", name)
		if (!StringUtils.isEmpty(content)) {
			bundle.putString("content", content)
		} else {
			bundle.putString("url", url)
		}
		bundle.putString("imageUrl", imageUrl)
		bundle.putString("date", date)
		bundle.putString("cardCategoryName", cardBagName)
		bundle.putInt("cardBagId", cardBagId)
		shareDialog.arguments = bundle
		val fragmentManager = fragmentManager
		val transaction = fragmentManager.beginTransaction()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			transaction.addSharedElement(iv_article_details, "cardImage")
			transaction.addSharedElement(tv_article_details_title, "cardTitle")

		}
		shareDialog.show(transaction, "")
	}

	private inner class MenuItemClickListener : MenuItem.OnMenuItemClickListener {

		override fun onMenuItemClick(item: MenuItem): Boolean {
			mWebView?.evaluateJavascript("getValue()") { value ->
				var content: String? = value.replace("\\\\u003C".toRegex(), "<").replace("\\\\\"".toRegex(), "")
				content = if (content != null) content.substring(1, content.length - 1) else null
				LogCat.d(content)
				when (item.itemId) {
					MENU_ITEM_NOTE -> if (checkLogin()) {
						val noteTitleDialog = NoteTitleDialog()
						val bundle = Bundle()
						bundle.putInt("withCardId", cardId)
						bundle.putString("title", name)
						bundle.putString("imageUrl", imageUrl)
						bundle.putString("content", content)
						noteTitleDialog.arguments = bundle
						noteTitleDialog.show(fragmentManager, "")
					}
					MENU_ITEM_SHARE -> gotoImageShare(mUrl, content)
				}
			}
			mActionMode?.finish()
			return true
		}
	}

	companion object {
		private const val MENU_ITEM_NOTE = 0
		private const val MENU_ITEM_SHARE = 1
	}
}
