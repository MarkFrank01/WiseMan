package com.zxcx.zhizhe.ui.card.cardDetails

import android.Manifest
import android.app.Activity
import android.app.SharedElementCallback
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.MultiTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.label.LabelActivity
import com.zxcx.zhizhe.ui.comment.CommentFragment
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.utils.GlideOptions.bitmapTransform
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.GoodView
import com.zxcx.zhizhe.widget.PermissionDialog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import java.io.File

class CardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

	private lateinit var mAdapter: CardDetailsAdapter
	private lateinit var mLayoutManager: LinearLayoutManager
	private var mList = arrayListOf<CardBean>()
	private var mCurrentPosition = 0
	private var mIsReturning = false
	private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
	private var mSourceName = ""
	private var commentFragment: CommentFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_card_details)
		EventBus.getDefault().register(this)
		initShareElement()
		initRecyclerView()
		initData()
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
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun onResume() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		super.onResume()
	}

	override fun initStatusBar() {
		mImmersionBar = ImmersionBar.with(this)
		mImmersionBar.keyboardEnable(true)
		mImmersionBar.init()
	}

	private fun initData() {
		mList = intent.getParcelableArrayListExtra<CardBean>("list")
		mCurrentPosition = intent.getIntExtra("currentPosition", 0)
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

	override fun getDataSuccess(bean: CardBean) {

	}

	override fun likeSuccess(bean: CardBean) {
		val position = mAdapter.data.indexOf(bean)
		toastShow("点赞成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.likeNum - 1).toString() + " +1")
		goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_like))
	}

	override fun collectSuccess(bean: CardBean) {
		val position = mAdapter.data.indexOf(bean)
		toastShow("收藏成功")
		postSuccess(bean)
		val goodView = GoodView(this)
		goodView.setTextColor(getColorForKotlin(R.color.button_blue))
		goodView.setText((bean.collectNum - 1).toString() + " +1")
		goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_collect))
	}

	override fun postSuccess(bean: CardBean) {
		val position = mAdapter.data.indexOf(bean)
		if (position != -1) {
			mAdapter.data[position] = bean
			mAdapter.notifyItemChanged(position)
		}
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
		mPresenter.setUserFollow(mAdapter.data[mCurrentPosition].authorId, 1, mAdapter.data[mCurrentPosition])
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
		mAdapter.setOnLoadMoreListener(this, rv_card_details)
		mAdapter.onItemChildClickListener = this
		rv_card_details.layoutManager = mLayoutManager
		rv_card_details.adapter = mAdapter
		PagerSnapHelper().attachToRecyclerView(rv_card_details)
		rv_card_details.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				val currentPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition()
				if (currentPosition != -1 && currentPosition < mAdapter.data.size && currentPosition != mCurrentPosition) {
					mPresenter.readCard(mAdapter.data[mCurrentPosition].id)
					mCurrentPosition = currentPosition
					val event = UpdateCardListPositionEvent(mCurrentPosition, mSourceName)
					EventBus.getDefault().post(event)
					val imageUrl = ZhiZheUtils.getHDImageUrl(mAdapter.data[mCurrentPosition].imageUrl)
					refreshBackground(imageUrl)
					/*ImageLoader.download(mActivity, imageUrl, R.drawable.default_card, iv_card_details_bg)
					Blurry.with(mActivity)
							.radius(10)
							.sampling(2)
							.color(Color.argb(216, 255, 255, 255))
							.async()
							.capture(iv_card_details_bg)
							.into(iv_card_details_bg)*/
				}
			}
		})
	}

	private fun refreshBackground(imageUrl: String?) {
		val multi = MultiTransformation(
				BlurTransformation(10),
				ColorFilterTransformation(Color.argb(216, 255, 255, 255)))
		GlideApp
				.with(mActivity)
				.load(imageUrl)
				.apply(bitmapTransform(multi))
				.into(iv_card_details_bg)
	}

	override fun setListener() {
		super.setListener()
		iv_common_close.setOnClickListener { onBackPressed() }
	}

	override fun onLoadMoreRequested() {
		val event = UpdateCardListPositionEvent(mCurrentPosition, mSourceName)
		EventBus.getDefault().post(event)
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
		val bean = mAdapter.data[position]
		when (view.id) {
			R.id.tv_item_card_details_label -> {
				startActivity(LabelActivity::class.java) {
					it.putExtra("id", bean.labelId)
					it.putExtra("name", bean.labelName)
				}
			}
			R.id.tv_item_card_details_author -> {
				startActivity(OtherUserActivity::class.java) {
					it.putExtra("id", bean.authorId)
				}
			}
			R.id.tv_item_card_details_goto_ad -> {
				startActivity(WebViewActivity::class.java) {
					it.putExtra("url", bean.adUrl)
					it.putExtra("title", bean.name)
					it.putExtra("imageUrl", bean.imageUrl)
					it.putExtra("isAD", true)
				}
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
					dialog.show(supportFragmentManager, "")
				}
			}
			R.id.iv_item_card_details_comment -> {
				commentFragment = CommentFragment()
				val bundle = Bundle()
				bundle.putInt("cardId", bean.id)
				commentFragment?.arguments = bundle

				val transaction = supportFragmentManager.beginTransaction()
				transaction.add(R.id.fl_card_details, commentFragment).commitAllowingStateLoss()
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
				val viewGroup = mAdapter.getViewByPosition(position, R.id.cl_item_card_details_content) as ViewGroup

				val rxPermissions = RxPermissions(this)
				rxPermissions
						.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
						.subscribe { permission ->
							when {
								permission.granted -> {
									// `permission.name` is granted !
									gotoImageShare(viewGroup)
								}
								permission.shouldShowRequestPermissionRationale -> // Denied permission without ask never again
									toastShow("权限已被拒绝！无法进行操作")
								else -> {
									// Denied permission with ask never again
									// Need to go to the settings
									val permissionDialog = PermissionDialog()
									permissionDialog.show(supportFragmentManager, "")
								}
							}
						}
			}
		}
	}

	override fun finishAfterTransition() {
		mIsReturning = true
		setResult(Activity.RESULT_OK)
		super.finishAfterTransition()
	}

	private fun gotoImageShare(viewGroup: ViewGroup) {
		mDisposable = Flowable.just(viewGroup)
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
						val shareCardDialog = ShareCardDialog()
						val bundle = Bundle()
						bundle.putString("imagePath", s)
						shareCardDialog.arguments = bundle
						shareCardDialog.show(supportFragmentManager, "")
					}

					override fun onError(t: Throwable) {
						hideLoading()
					}

					override fun onComplete() {

					}
				})
	}

	private fun initShareElement() {
		postponeEnterTransition()
		setEnterSharedElementCallback(mCallback)
//		setExitSharedElementCallback(mExitCallback)
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

	private val mExitCallback = object : SharedElementCallback() {

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
