package com.zxcx.zhizhe.ui.card.cardDetails

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.load.MultiTransformation
import com.pixplicity.htmlcompat.HtmlCompat
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.label.LabelActivity
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.ui.comment.CommentFragment
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.utils.GlideOptions.bitmapTransform
import com.zxcx.zhizhe.widget.GoodView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_single_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import java.io.File

class SingleCardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View {

	private lateinit var mCardBean: CardBean
	private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
	private var commentFragment: CommentFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_single_card_details)
		EventBus.getDefault().register(this)
		initData()
	}

	override fun onResume() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		super.onResume()
	}

	override fun onBackPressed() {
		if (commentFragment?.isAdded == true) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction.remove(commentFragment).commitAllowingStateLoss()
			commentFragment = null
		} else {
			super.onBackPressed()
		}
	}

	override fun onDestroy() {
		mPresenter.readCard(mCardBean.id)
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun initStatusBar() {}

	private fun initData() {
		mCardBean = intent.getParcelableExtra("cardBean")
		if (mCardBean.name == null) {
			mPresenter.getCardDetails(mCardBean.id)
		} else {
			refreshView()
		}
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
		tv_item_card_details_author.visibility = if (mCardBean.adUrl.isEmpty()) View.VISIBLE else View.GONE
		cb_item_card_details_follow.visibility = if (mCardBean.adUrl.isEmpty()) View.VISIBLE else View.GONE
		tv_item_card_details_goto_ad.visibility = if (mCardBean.adUrl.isNotEmpty()) View.VISIBLE else View.GONE

		val multi = MultiTransformation(
				BlurTransformation(10),
				ColorFilterTransformation(Color.argb(216, 255, 255, 255)))
		GlideApp
				.with(mActivity)
				.load(imageUrl)
				.apply(bitmapTransform(multi))
				.into(iv_card_details_bg)


		val fromHtml = HtmlCompat.fromHtml(mActivity, mCardBean.content, 0)
		tv_item_card_details_content.movementMethod = LinkMovementMethod.getInstance()
		tv_item_card_details_content.text = fromHtml
	}

	override fun createPresenter(): CardDetailsPresenter {
		return CardDetailsPresenter(this)
	}

	override fun getDataSuccess(bean: CardBean) {
		mCardBean = bean
		refreshView()
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
			startActivity(LabelActivity::class.java) {
				it.putExtra("id", mCardBean.labelId)
				it.putExtra("name", mCardBean.labelName)
			}
		}
		tv_item_card_details_author.setOnClickListener {
			startActivity(OtherUserActivity::class.java) {
				it.putExtra("id", mCardBean.authorId)
			}
		}
		tv_item_card_details_goto_ad.setOnClickListener {
			startActivity(WebViewActivity::class.java) {
				it.putExtra("url", mCardBean.adUrl)
				it.putExtra("title", mCardBean.name)
				it.putExtra("imageUrl", mCardBean.imageUrl)
				it.putExtra("isAD", true)
			}
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
			commentFragment = CommentFragment()
			val bundle = Bundle()
			bundle.putInt("cardId", mCardBean.id)
			commentFragment?.arguments = bundle

			val transaction = supportFragmentManager.beginTransaction()
			transaction.add(R.id.cl_card_details, commentFragment).commitAllowingStateLoss()
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
			gotoImageShare()
		}
	}

	private fun gotoImageShare() {
		mDisposable = Flowable.just(cl_item_card_details_content)
				.subscribeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe { subscription -> showLoading() }
				.observeOn(Schedulers.io())
				.map { view ->
					val bitmap = ScreenUtils.getBitmapAndQRByView(view)
					val fileName = FileUtil.getRandomImageName()
					FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName)
					val path = FileUtil.PATH_BASE + fileName
					Luban.with(mActivity)
							.load(File(path))                     //传入要压缩的图片
							.get()
							.path//启动压缩
					//                    return path;
				}
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(object : DisposableSubscriber<String>() {

					override fun onNext(s: String) {
						// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
						//启动分享
						hideLoading()
						val shareCardDialog = ShareDialog()
						val bundle = Bundle()
						bundle.putString("imagePath", s)
						shareCardDialog.arguments = bundle
						shareCardDialog.show(fragmentManager, "")
					}

					override fun onError(t: Throwable) {
						hideLoading()
					}

					override fun onComplete() {

					}
				})
	}
}
