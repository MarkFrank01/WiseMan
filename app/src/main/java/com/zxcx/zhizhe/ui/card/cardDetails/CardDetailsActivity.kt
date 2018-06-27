package com.zxcx.zhizhe.ui.card.cardDetails

import android.app.SharedElementCallback
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ShareCardDialog
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.GoodView
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemChildClickListener {

    private lateinit var mAdapter: CardDetailsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mList = arrayListOf<CardBean>()
    private var mCurrentPosition = 0
    private var mIsReturning = false
    private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
    private var mSourceName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        initShareElement()
        initRecyclerView()
        initData()
    }

    override fun onResume() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        super.onResume()
    }

    override fun initStatusBar() {}

    private fun initShareElement() {
        postponeEnterTransition()
        setEnterSharedElementCallback(mCallback)
    }

    private fun initData() {
        mList = intent.getParcelableArrayListExtra<CardBean>("list")
        mCurrentPosition = intent.getIntExtra("currentPosition",0)
        mSourceName = intent.getStringExtra("sourceName")
        mAdapter.setNewData(mList)
        rv_card_details.scrollToPosition(mCurrentPosition)
        rv_card_details.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rv_card_details.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun createPresenter(): CardDetailsPresenter {
        return CardDetailsPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {

    }

    override fun likeSuccess(bean: CardBean) {
        val position = mAdapter.data.indexOf(bean)
        toastShow("点赞成功")
        postSuccess(bean)
        val goodView = GoodView(this)
        goodView.setTextColor(getColorForKotlin(R.color.button_blue))
        goodView.setText((bean.likeNum-1).toString()+" +1")
        goodView.show(rv_card_details.getChildAt(position).findViewById(R.id.cb_item_card_details_like))
    }

    override fun collectSuccess(bean: CardBean) {
        val position = mAdapter.data.indexOf(bean)
        toastShow("收藏成功")
        postSuccess(bean)
        val goodView = GoodView(this)
        goodView.setTextColor(getColorForKotlin(R.color.button_blue))
        goodView.setText((bean.collectNum-1).toString()+" +1")
        goodView.show(rv_card_details.getChildAt(position).findViewById(R.id.cb_item_card_details_collect))
    }

    override fun postSuccess(bean: CardBean) {
        val position = mAdapter.data.indexOf(bean)
        mAdapter.data[position] = bean
        mAdapter.notifyItemChanged(position)
    }

    override fun postFail(msg: String) {
        toastShow(msg)
    }

    override fun followSuccess(bean: CardBean) {
        bean.followType = if (bean.followType == 0) 1 else 0
        postSuccess(bean)
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnFollowConfirmEvent) {
        //取消关注
        mPresenter.setUserFollow(mAdapter.data[mCurrentPosition].authorId, 1,mAdapter.data[mCurrentPosition])
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AddCardDetailsListEvent) {
        if (mSourceName == event.sourceName) {
            mAdapter.addData(event.list)
        }
    }

    private fun initRecyclerView() {
        mLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = CardDetailsAdapter(mList)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_card_details)
        mAdapter.onItemChildClickListener = this
        rv_card_details.layoutManager = mLayoutManager
        rv_card_details.adapter = mAdapter
        PagerSnapHelper().attachToRecyclerView(rv_card_details)
        rv_card_details.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                if(currentPosition != -1 && currentPosition < mAdapter.data.size && currentPosition != mCurrentPosition) {
                    mCurrentPosition = currentPosition
                    val event = UpdateCardListPositionEvent(mCurrentPosition, mSourceName)
                    EventBus.getDefault().post(event)
                    val imageUrl = ZhiZheUtils.getHDImageUrl(mAdapter.data[mCurrentPosition].imageUrl)
                    ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_card_details_bg)
                    Blurry.with(mActivity)
                            .radius(10)
                            .sampling(2)
                            .color(Color.argb(216, 255, 255, 255))
                            .async()
                            .capture(iv_card_details_bg)
                            .into(iv_card_details_bg)
                }
            }
        })
    }

    override fun setListener() {
        super.setListener()
        iv_card_details_back.setOnClickListener { onBackPressed() }
    }

    override fun onLoadMoreRequested() {
        val event = UpdateCardListPositionEvent(mCurrentPosition, mSourceName)
        EventBus.getDefault().post(event)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
        val bean = mAdapter.data[position]
        when(view.id){
            R.id.tv_item_card_details_label -> {
                //todo 标签页
            }
            R.id.tv_item_card_details_author -> {
                startActivity(OtherUserActivity::class.java) {
                    it.putExtra("id",bean.authorId)
                }
            }
            R.id.tv_item_card_details_goto_ad -> {
                //todo 广告
            }
            R.id.cb_item_card_details_follow -> {
                view as CheckBox
                view.isChecked = !view.isChecked
                if (!checkLogin()) {
                    return
                }
                if (mUserId == bean.authorId) {
                    toastShow("无法关注自己")
                    return
                }
                if (!view.isChecked) {
                    //关注
                    mPresenter.setUserFollow(bean.authorId, 0, bean)
                } else {
                    //取消关注弹窗
                    val dialog = UnFollowConfirmDialog()
                    val bundle = Bundle()
                    bundle.putInt("userId", bean.authorId)
                    dialog.arguments = bundle
                    dialog.show(fragmentManager, "")
                }
            }
            R.id.iv_item_card_details_comment -> {
                //todo 评论
            }
            R.id.cb_item_card_details_collect -> {
                //checkBox点击之后选中状态就已经更改了
                view as CheckBox
                view.isChecked = !view.isChecked
                if (mUserId == bean.authorId) {
                    toastShow("不能收藏自己哦")
                    return
                }
                if (!view.isChecked) {
                    if (checkLogin()) {
                        mPresenter.addCollectCard(bean.id)
                    }
                } else {
                    mPresenter.removeCollectCard(bean.id)
                }
            }
            R.id.cb_item_card_details_like -> {
                //checkBox点击之后选中状态就已经更改了
                view as CheckBox
                view.isChecked = !view.isChecked
                if (mUserId == bean.authorId) {
                    toastShow("不能点赞自己哦")
                    return
                }
                if (!view.isChecked) {
                    if (checkLogin()) {
                        mPresenter.likeCard(bean.id)
                    }
                } else {
                    mPresenter.removeLikeCard(bean.id)
                }
            }
            R.id.iv_item_card_details_share -> {
                //todo 分享
            }
        }
    }

    override fun finishAfterTransition() {
        mIsReturning = true
        super.finishAfterTransition()
    }

    private fun gotoImageShare(url: String?, content: String?) {
        val shareDialog = ShareCardDialog()
        val bundle = Bundle()
        /*bundle.putString("name", name)
        if (!StringUtils.isEmpty(content)) {
            bundle.putString("content", content)
        } else {
            bundle.putString("url", url)
        }
        bundle.putString("imageUrl", imageUrl)
        bundle.putString("date", date)
        bundle.putString("authorName", author)
        bundle.putString("cardCategoryName", cardBagName)
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
        shareDialog.show(transaction, "")*/
    }

    private val mCallback = object : SharedElementCallback() {

        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (mIsReturning) {
                names.clear()
                sharedElements.clear()
                val view = mLayoutManager.findViewByPosition(mCurrentPosition)
                val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_details_icon)
                val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_details_title)
                val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_details_category)
                val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_details_label)
                names.add(cardImg.transitionName)
                sharedElements[cardImg.transitionName] = cardImg
                names.add(cardTitle.transitionName)
                sharedElements[cardTitle.transitionName] = cardTitle
                names.add(cardCategory.transitionName)
                sharedElements[cardCategory.transitionName] = cardCategory
                names.add(cardLabel.transitionName)
                sharedElements[cardLabel.transitionName] = cardLabel
            }
        }
    }
}
