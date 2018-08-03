package com.zxcx.zhizhe.ui.my.creation.creationDetails

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import butterknife.ButterKnife
import com.bumptech.glide.load.MultiTransformation
import com.pixplicity.htmlcompat.HtmlCompat
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.DeleteCreationEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.utils.*
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_draft_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DraftCardDetailsActivity : MvpActivity<RejectDetailsPresenter>(), RejectDetailsContract.View {

	private lateinit var cardBean: CardBean

	override fun onCreate(savedInstanceState: Bundle?) {
		setContentView(R.layout.activity_draft_card_details)
		super.onCreate(savedInstanceState)
		ButterKnife.bind(this)
		EventBus.getDefault().register(this)

		initData()
		initView()

		mPresenter.getRejectDetails(cardBean.id)
	}

	override fun initStatusBar() {

	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun createPresenter(): RejectDetailsPresenter {
		return RejectDetailsPresenter(this)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: CommitCardReviewEvent) {
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: DeleteCreationEvent) {
		finish()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: SaveDraftSuccessEvent) {
		onReload(null)
	}

	override fun onReload(v: View?) {
		mPresenter.getRejectDetails(cardBean.id)
	}

	override fun getDataSuccess(bean: CardBean) {
		//进入时只有id的时候，在这里初始化界面
		cardBean = bean

		tv_draft_details_title.text = bean.name
		tv_draft_details_category.text = bean.categoryName
		tv_draft_details_label.text = bean.getLabelName()
		val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_draft_details)
	}

	override fun postSuccess() {
		toastShow("提交成功")
		EventBus.getDefault().post(SaveDraftSuccessEvent())
		onBackPressed()
	}

	override fun postFail(msg: String?) {
		toastError(msg)
	}

	private fun initData() {
		cardBean = intent.getParcelableExtra("cardBean")
	}

	private fun initView() {
		if (!StringUtils.isEmpty(cardBean.name))
			tv_draft_details_title.text = cardBean.name
		if (!StringUtils.isEmpty(cardBean.categoryName))
			tv_draft_details_category.text = cardBean.categoryName
		if (!StringUtils.isEmpty(cardBean.getLabelName()))
			tv_draft_details_label.text = cardBean.getLabelName()
		if (!StringUtils.isEmpty(cardBean.imageUrl)) {
			val imageUrl = ZhiZheUtils.getHDImageUrl(cardBean.imageUrl)
			ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_draft_details)
		}

		val multi = MultiTransformation(
				ColorFilterTransformation(Color.argb(216, 255, 255, 255)))
		GlideApp
				.with(mActivity)
				.load(cardBean.imageUrl)
				.apply(GlideOptions.bitmapTransform(multi))
				.into(iv_card_details_bg)

		initWebView()
	}

	private fun initWebView() {
		val fromHtml = HtmlCompat.fromHtml(mActivity, cardBean.content, 0)
		tv_draft_details_content.movementMethod = LinkMovementMethod.getInstance()
		tv_draft_details_content.text = fromHtml
	}

	override fun setListener() {
		iv_common_close.setOnClickListener {
			onBackPressed()
		}

		iv_draft_details_edit.setOnClickListener {
			startActivity(CreationEditorActivity::class.java) {
				it.putExtra("cardId", cardBean.id)
				it.putExtra("type", 2)
			}
		}

		iv_draft_details_commit.setOnClickListener {
			val dialog = SubmitCreationDialog()
			dialog.mListener = {
				mPresenter.submitReview(cardBean.id, 0)
			}
			dialog.show(supportFragmentManager, "")
		}
	}
}
