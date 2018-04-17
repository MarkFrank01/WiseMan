package com.zxcx.zhizhe.ui.card.card.cardDetails

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
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.classify.subject.SubjectActivity
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GoodView
import kotlinx.android.synthetic.main.activity_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class CardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View {

    private var mWebView: WebView? = null
    private var cardId: Int = 0
    private var cardBagId: Int = 0
    private var subjectId: Int = 0
    private var mUserId: Int = 0
    private var mAuthorId: Int = 0
    private var name: String? = null
    private var cardBagName: String? = null
    private var subjectName: String? = null
    private var imageUrl: String? = null
    private var collectStatus = false
    private var isUnCollect = false
    private var likeStatus = false
    private var isUnLike = false
    private var date: String? = null
    private var author: String? = null
    private var mActionMode: ActionMode? = null
    private var mUrl: String? = null
    private lateinit var startDate: Date
    private var loadService2: LoadService<*>? = null
    private var loadSir2: LoadSir? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)

        val para = iv_card_details.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.height = screenWidth * 9 / 16
        iv_card_details.layoutParams = para

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

    override fun createPresenter(): CardDetailsPresenter {
        return CardDetailsPresenter(this)
    }

    override fun onReload(v: View) {
        mPresenter.getCardDetails(cardId)
        mWebView?.reload()
    }

    override fun getDataSuccess(bean: CardDetailsBean) {
        collectStatus = bean.isCollect
        likeStatus = bean.isLike
        postSuccess(bean)
        //进入时只有id的时候，在这里初始化界面
        name = bean.name
        imageUrl = bean.imageUrl
        date = DateTimeUtils.getDateString(bean.date)
        author = bean.authorName
        tv_card_details_title.text = name
        tv_card_details_info.text = getString(R.string.tv_card_info, date, author)
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_card_details)
        val ad = bean.ad
        if (ad != null){
            fl_card_details_ad.visibility = View.VISIBLE
            ImageLoader.load(mActivity, ad.content, R.drawable.default_card, iv_card_details_ad)
            fl_card_details_ad.setOnClickListener {
                startActivity(WebViewActivity::class.java,{
                    it.putExtra("title", ad.description)
                    it.putExtra("url", ad.behavior)
                })
            }
        }
    }

    override fun likeSuccess(bean: CardDetailsBean) {
        toastShow("点赞成功")
        postSuccess(bean)
        val goodView = GoodView(this)
        goodView.setTextColor(getColorForKotlin(R.color.button_blue))
        goodView.setText((bean.likeNum-1).toString()+" +1")
        goodView.show(cb_card_details_like)
    }

    override fun collectSuccess(bean: CardDetailsBean) {
        toastShow("收藏成功")
        postSuccess(bean)
        val goodView = GoodView(this)
        goodView.setTextColor(getColorForKotlin(R.color.button_blue))
        goodView.setText((bean.collectNum-1).toString()+" +1")
        goodView.show(cb_card_details_collect)
    }

    override fun postSuccess(bean: CardDetailsBean) {
        isUnCollect = collectStatus != bean.isCollect() && !bean.isCollect()
        isUnLike = likeStatus != bean.isLike() && !bean.isLike()

        val collectNum = bean.collectNum
        val likeNum = bean.likeNum
        val unLikeNum = bean.unLikeNum
        cardBagName = bean.cardBagName
        cardBagId = bean.cardBagId
        subjectName = bean.subjectName
        subjectId = bean.subjectId
        tv_card_details_card_bag.text = if (subjectName.isNullOrEmpty()) cardBagName else subjectName
        cb_card_details_collect.text = collectNum.toString()
        cb_card_details_like.text = likeNum.toString()
        cb_card_details_un_like.text = unLikeNum.toString()
        cb_card_details_collect.isChecked = bean.isCollect
        cb_card_details_like.isChecked = bean.isLike
        cb_card_details_un_like.isChecked = bean.isUnLike
        mAuthorId = bean.authorId
        ImageLoader.load(mActivity, bean.authorIcon, R.drawable.default_header, iv_item_rank_user)
        tv_item_rank_user_name.text = bean.authorName
        tv_item_rank_user_card.text = bean.authorCardNum
        tv_item_rank_user_fans.text = bean.authorFansNum
        tv_item_rank_user_read.text = bean.authorReadNum
        if (bean.followType == 0) {
            cb_card_details_follow.text = "关注"
            cb_card_details_follow.isChecked = false
        } else {
            cb_card_details_follow.text = "已关注"
            cb_card_details_follow.isChecked = true
        }
    }

    override fun postFail(msg: String) {
        toastShow(msg)
    }

    override fun followSuccess() {
        if (cb_card_details_follow.isChecked) {
            //取消成功
            cb_card_details_follow.text = "关注"
            cb_card_details_follow.isChecked = false
        } else {
            //关注成功
            toastShow("关注成功")
            cb_card_details_follow.text = "已关注"
            cb_card_details_follow.isChecked = true
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
        iv_card_details_back.setOnClickListener {
            onBackPressed()
        }
        fl_card_details_card_bag.setOnClickListener {
            if (subjectName.isNullOrEmpty()) {
                startActivity(CardBagActivity::class.java, {
                    it.putExtra("name", cardBagName)
                    it.putExtra("id", cardBagId)
                })
            }else{
                startActivity(SubjectActivity::class.java, {
                    it.putExtra("name", cardBagName)
                    it.putExtra("id", cardBagId)
                })
            }
        }
        cb_card_details_follow.setOnClickListener {
            cb_card_details_follow.isChecked = !cb_card_details_follow.isChecked
            if (!checkLogin()) {
                return@setOnClickListener
            }
            if (mUserId == mAuthorId) {
                toastShow("无法关注自己")
                return@setOnClickListener
            }
            if (!cb_card_details_follow.isChecked) {
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
        rl_card_details_bottom.setOnClickListener {
            startActivity(OtherUserActivity::class.java,{
                it.putExtra("name", author)
                it.putExtra("id", mAuthorId)
            })
        }
        iv_card_details_share.setOnClickListener {
            gotoShare(mUrl, null)
        }
        cb_card_details_un_like.setOnClickListener {
            //checkBox点击之后选中状态就已经更改了
            cb_card_details_un_like.isChecked = !cb_card_details_un_like.isChecked
            if (!cb_card_details_un_like.isChecked) {
                if (checkLogin()) {
                    mPresenter.unLikeCard(cardId)
                }
            } else {
                mPresenter.removeUnLikeCard(cardId)
            }
        }
        cb_card_details_like.setOnClickListener {
            //checkBox点击之后选中状态就已经更改了
            cb_card_details_like.isChecked = !cb_card_details_like.isChecked
            if (mUserId == mAuthorId) {
                toastShow("不能点赞自己哦")
                return@setOnClickListener
            }
            if (!cb_card_details_like.isChecked) {
                if (checkLogin()) {
                    mPresenter.likeCard(cardId)
                }
            } else {
                mPresenter.removeLikeCard(cardId)
            }
        }
        cb_card_details_collect.setOnClickListener {
            //checkBox点击之后选中状态就已经更改了
            cb_card_details_collect.isChecked = !cb_card_details_collect.isChecked
            if (mUserId == mAuthorId) {
                toastShow("不能收藏自己哦")
                return@setOnClickListener
            }
            if (!cb_card_details_collect.isChecked) {
                if (checkLogin()) {
                    mPresenter.addCollectCard(cardId)
                }
            } else {
                mPresenter.removeCollectCard(cardId)
            }
        }
    }

    private fun initData() {
        cardId = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
        imageUrl = intent.getStringExtra("imageUrl")
        date = intent.getStringExtra("date")
        author = intent.getStringExtra("author")
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
    }

    private fun initView() {
        if (!StringUtils.isEmpty(name))
            tv_card_details_title.text = name
        if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(date))
            tv_card_details_info.text = getString(R.string.tv_card_info, date, author)
        if (!StringUtils.isEmpty(imageUrl))
            ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_card_details)
        initWebView()

        initLoadSir()
    }

    private fun initWebView() {
        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mWebView?.layoutParams = params

        mWebView?.webViewClient = object : WebViewClient() {

            internal var isError = false

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
                ll_card_details_bottom.visibility = View.VISIBLE
                rl_card_details_bottom.visibility = View.VISIBLE
                view_line.visibility = View.VISIBLE
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                isError = true
                loadService.showSuccess()
                if (loadService2 == null) {
                    loadService2 = loadSir2?.register(sv_card_details, this@CardDetailsActivity)
                }
                loadService2?.showCallback(CardDetailsNetworkErrorCallback::class.java)
            }
        }
        mWebView?.isFocusable = false
        fl_card_details.addView(mWebView)
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

    private fun gotoShare(url: String?, content: String?) {
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
        bundle.putString("author", author)
        bundle.putString("cardBagName", cardBagName)
        bundle.putInt("cardBagId", cardBagId)
        shareDialog.arguments = bundle
        val fragmentManager = fragmentManager
        val transaction = fragmentManager.beginTransaction()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transaction.addSharedElement(iv_card_details, "cardImage")
            transaction.addSharedElement(tv_card_details_title, "cardTitle")
            transaction.addSharedElement(tv_card_details_info, "cardInfo")
            transaction.addSharedElement(tv_card_details_card_bag, "cardBag")

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
                    MENU_ITEM_SHARE -> gotoShare(mUrl, content)
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
