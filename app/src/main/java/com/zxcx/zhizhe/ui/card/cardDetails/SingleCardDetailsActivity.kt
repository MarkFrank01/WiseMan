package com.zxcx.zhizhe.ui.card.cardDetails

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.load.MultiTransformation
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ShareCardDialog
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.utils.GlideOptions.bitmapTransform
import com.zxcx.zhizhe.widget.GoodView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_single_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SingleCardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View {

	private lateinit var mCardBean: CardBean
	private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_single_card_details)
		initData()
	}

	override fun onResume() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		super.onResume()
	}

	override fun initStatusBar() {}

	private fun initData() {
		mCardBean = intent.getParcelableExtra("cardBean")

		refreshView()
	}

	private fun refreshView() {
		val imageUrl = ZhiZheUtils.getHDImageUrl(mCardBean.imageUrl)
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_item_card_details_icon)
		tv_item_card_details_title.text = mCardBean.name
		tv_item_card_details_category.text = mCardBean.categoryName
		tv_item_card_details_label.text = mCardBean.labelName
		tv_item_card_details_author.text = mCardBean.authorName
		tv_item_card_details_goto_ad.text = mCardBean.authorName
		tv_item_card_details_comment.text = mCardBean.commentNum.toString()
		tv_item_card_details_collect.text = mCardBean.collectNum.toString()
		tv_item_card_details_like.text = mCardBean.likeNum.toString()
		cb_item_card_details_follow.isChecked = mCardBean.isFollow
		cb_item_card_details_collect.isChecked = mCardBean.isCollect
		cb_item_card_details_like.isChecked = mCardBean.isLike

		//扩大10dp点击区域
		cb_item_card_details_follow.expandViewTouchDelegate(ScreenUtils.dip2px(8f))
		iv_item_card_details_comment.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
		cb_item_card_details_collect.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
		cb_item_card_details_like.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
		iv_item_card_details_share.expandViewTouchDelegate(ScreenUtils.dip2px(10f))

		//是否广告
		tv_item_card_details_author.visibility = if (mCardBean.adUrl == null) View.VISIBLE else View.GONE
		cb_item_card_details_follow.visibility = if (mCardBean.adUrl == null) View.VISIBLE else View.GONE
		tv_item_card_details_goto_ad.visibility = if (mCardBean.adUrl != null) View.VISIBLE else View.GONE

		val multi = MultiTransformation(
				BlurTransformation(10),
				ColorFilterTransformation(Color.argb(216, 255, 255, 255)))
		GlideApp
				.with(mActivity)
				.load(imageUrl)
				.apply(bitmapTransform(multi))
				.into(iv_card_details_bg)
	}

	override fun createPresenter(): CardDetailsPresenter {
		return CardDetailsPresenter(this)
	}

	override fun getDataSuccess(list: MutableList<CardBean>) {

	}

	override fun likeSuccess(bean: CardBean) {
		toastShow("点赞成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.likeNum - 1).toString() + " +1")
		goodView.show(cb_item_card_details_like)
	}

	override fun collectSuccess(bean: CardBean) {
		toastShow("收藏成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.collectNum - 1).toString() + " +1")
		goodView.show(cb_item_card_details_collect)
	}

	override fun postSuccess(bean: CardBean) {
		mCardBean = bean
		refreshView()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}

	override fun followSuccess(bean: CardBean) {
		bean.isFollow = !bean.isFollow
		postSuccess(bean)
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnFollowConfirmEvent) {
		//取消关注
		mPresenter.setUserFollow(mCardBean.authorId, 1, mCardBean)
	}

	override fun setListener() {
		super.setListener()
		iv_card_details_back.setOnClickListener { onBackPressed() }
		tv_item_card_details_label.setOnClickListener {
			//todo 标签页
		}
		tv_item_card_details_author.setOnClickListener {
			startActivity(OtherUserActivity::class.java) {
				it.putExtra("id", mCardBean.authorId)
			}
		}
		tv_item_card_details_goto_ad.setOnClickListener {
			//todo 广告
		}
		cb_item_card_details_follow.setOnClickListener {
			it as CheckBox
			it.isChecked = !it.isChecked
			if (!checkLogin()) {
				return@setOnClickListener
			}
			if (mUserId == mCardBean.authorId) {
				toastShow("无法关注自己")
				return@setOnClickListener
			}
			if (!it.isChecked) {
				//关注
				mPresenter.setUserFollow(mCardBean.authorId, 0, mCardBean)
			} else {
				//取消关注弹窗
				val dialog = UnFollowConfirmDialog()
				val bundle = Bundle()
				bundle.putInt("userId", mCardBean.authorId)
				dialog.arguments = bundle
				dialog.show(fragmentManager, "")
			}
		}
		iv_item_card_details_comment.setOnClickListener {
			//todo 评论
		}
		cb_item_card_details_collect.setOnClickListener {
			//checkBox点击之后选中状态就已经更改了
			it as CheckBox
			it.isChecked = !it.isChecked
			if (mUserId == mCardBean.authorId) {
				toastShow("不能收藏自己哦")
				return@setOnClickListener
			}
			if (!it.isChecked) {
				if (checkLogin()) {
					mPresenter.addCollectCard(mCardBean.id)
				}
			} else {
				mPresenter.removeCollectCard(mCardBean.id)
			}
		}
		cb_item_card_details_like.setOnClickListener {
			//checkBox点击之后选中状态就已经更改了
			it as CheckBox
			it.isChecked = !it.isChecked
			if (mUserId == mCardBean.authorId) {
				toastShow("不能点赞自己哦")
				return@setOnClickListener
			}
			if (!it.isChecked) {
				if (checkLogin()) {
					mPresenter.likeCard(mCardBean.id)
				}
			} else {
				mPresenter.removeLikeCard(mCardBean.id)
			}
		}
		iv_item_card_details_share.setOnClickListener {
			//todo 分享
		}
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
		bundle.putString("categoryName", cardBagName)
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
}
