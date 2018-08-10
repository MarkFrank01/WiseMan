package com.zxcx.zhizhe.ui.card.cardDetails

import android.Manifest
import android.app.Activity
import android.app.SharedElementCallback
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.MultiTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.*
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
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import java.io.File

/**
 * 卡片详情列表页面
 */

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
				val imageUrl = ZhiZheUtils.getHDImageUrl(mAdapter.data[mCurrentPosition].imageUrl)
				refreshBackground(imageUrl)
				return true
			}
		})
	}

	override fun createPresenter(): CardDetailsPresenter {
		return CardDetailsPresenter(this)
	}

	override fun getDataSuccess(bean: CardBean) {

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
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnFollowConfirmEvent) {
		//取消关注
		val event = UpdateCardListEvent(mCurrentPosition, mSourceName, mAdapter.data[mCurrentPosition])
		EventBus.getDefault().post(event)
		(mAdapter.getViewByPosition(mCurrentPosition, R.id.cb_item_card_details_follow) as CheckBox).isChecked = false
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
				}
			}
		})
	}

	private fun refreshBackground(imageUrl: String?) {
		val multi = MultiTransformation(
				ColorFilterTransformation(getColorForKotlin(R.color.bg_card_details)))
		/*val multi = MultiTransformation(
				ColorFilterTransformation(Color.argb(229, 255, 255, 255)))*/
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
				setFollow(view, bean, position)
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
				setCollect(view, bean, position)
			}
			R.id.cb_item_card_details_like -> {
				//checkBox点击之后选中状态就已经更改了
				setLike(view, bean, position)
			}
			R.id.iv_item_card_details_share -> {

				val rxPermissions = RxPermissions(this)
				rxPermissions
						.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
						.subscribe { permission ->
							when {
								permission.granted -> {
									// `permission.name` is granted !
									gotoImageShare()
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

	private fun setLike(view: View, bean: CardBean, position: Int) {
		view as CheckBox
		if (mUserId == bean.authorId) {
			toastShow("不能点赞自己哦")
			view.isChecked = !view.isChecked
			return
		}
		if (checkLogin()) {
			bean.isLike = !bean.isLike
			(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_like) as TextView)
					.isEnabled = bean.isLike
			if (view.isChecked) {
				val goodView = GoodView(this)
				goodView.setTextColor(getColorForKotlin(R.color.button_blue))
				goodView.setText("+1")
				goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_like))
				bean.likeNum += 1
				(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_like) as TextView)
						.text = bean.likeNum.toString()
				mPresenter.likeCard(bean.id)
			} else {
				val goodView = GoodView(this)
				goodView.setTextColor(getColorForKotlin(R.color.button_blue))
				goodView.setText("-1")
				goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_like))
				bean.likeNum -= 1
				(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_like) as TextView)
						.text = bean.likeNum.toString()
				mPresenter.removeLikeCard(bean.id)
			}
			val event = UpdateCardListEvent(mCurrentPosition, mSourceName, bean)
			EventBus.getDefault().post(event)
		} else {
			view.isChecked = !view.isChecked
		}
	}

	private fun setCollect(view: View, bean: CardBean, position: Int) {
		view as CheckBox
		if (mUserId == bean.authorId) {
			toastShow("不能收藏自己哦")
			view.isChecked = !view.isChecked
			return
		}
		if (checkLogin()) {
			bean.isCollect = !bean.isCollect
			(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_collect) as TextView)
					.isEnabled = bean.isCollect
			if (view.isChecked) {
				val goodView = GoodView(this)
				goodView.setTextColor(getColorForKotlin(R.color.button_blue))
				goodView.setText("+1")
				goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_collect))
				bean.collectNum += 1
				(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_collect) as TextView)
						.text = bean.collectNum.toString()
				mPresenter.addCollectCard(bean.id)
			} else {
				val goodView = GoodView(this)
				goodView.setTextColor(getColorForKotlin(R.color.button_blue))
				goodView.setText("-1")
				goodView.show(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_collect))
				bean.collectNum -= 1
				(mAdapter.getViewByPosition(position, R.id.tv_item_card_details_collect) as TextView)
						.text = bean.collectNum.toString()
				mPresenter.removeCollectCard(bean.id)
			}
			val event = UpdateCardListEvent(mCurrentPosition, mSourceName, bean)
			EventBus.getDefault().post(event)
		} else {
			view.isChecked = !view.isChecked
		}
	}

	private fun setFollow(view: View, bean: CardBean, position: Int) {
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
			(mAdapter.getViewByPosition(position, R.id.cb_item_card_details_follow) as CheckBox).isChecked = true
			mPresenter.setUserFollow(bean.authorId, 0, bean)
			val event = UpdateCardListEvent(mCurrentPosition, mSourceName, bean)
			EventBus.getDefault().post(event)
		} else {
			//取消关注弹窗
			val dialog = UnFollowConfirmDialog()
			val bundle = Bundle()
			bundle.putInt("userId", bean.authorId)
			dialog.arguments = bundle
			dialog.show(supportFragmentManager, "")
		}
	}

	override fun finishAfterTransition() {
		mIsReturning = true
		setResult(Activity.RESULT_OK)
		super.finishAfterTransition()
	}

	private fun gotoImageShare() {
		mDisposable = Flowable.just(fl_card_details)
				.subscribeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe { subscription -> showLoading() }
				.map {
					iv_share_qr.visibility = View.VISIBLE
					iv_common_close.visibility = View.GONE
					it
				}
				.observeOn(Schedulers.io())
				.map { view ->
					val bitmap = ScreenUtils.getBitmapByView(view)
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
				.map {
					iv_share_qr.visibility = View.GONE
					iv_common_close.visibility = View.VISIBLE
					it
				}
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
