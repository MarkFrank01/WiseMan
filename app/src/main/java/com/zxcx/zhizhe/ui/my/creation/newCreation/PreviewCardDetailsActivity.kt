package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import com.bumptech.glide.load.MultiTransformation
import com.gyf.barlibrary.ImmersionBar
import com.pixplicity.htmlcompat.HtmlCompat
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsContract
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.utils.GlideOptions.bitmapTransform
import com.zxcx.zhizhe.widget.UploadingDialog
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_preview_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创作卡片预览页面
 */

class PreviewCardDetailsActivity : MvpActivity<CardDetailsPresenter>(), CardDetailsContract.View {

    private lateinit var mUploadingDialog: UploadingDialog

    private lateinit var mCardBean: CardBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_card_details)
        EventBus.getDefault().register(this)
        mUploadingDialog = UploadingDialog()

        initData()
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

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
        if (mCardBean.labelName!=""&&mCardBean.labelName.isNotEmpty()) {
            tv_item_card_details_label.text = mCardBean.getLabelName()
        }
        if (mCardBean.secondCollectionTitle.isNotEmpty()) {
            tv_item_card_details_label2.text = mCardBean.getSecondLabelName()
        }

        val multi = MultiTransformation(
                ColorFilterTransformation(getColorForKotlin(R.color.bg_card_details)))
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

    override fun postSuccess(bean: CardBean) {
        runOnUiThread {
            mUploadingDialog.setSuccess(true)
        }
        Handler().postDelayed({
            EventBus.getDefault().post(CommitCardReviewEvent())
            startActivity(CreationActivity::class.java) {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                it.putExtra("goto", 1)
            }
            Utils.closeInputMethod(mActivity)
            finish()
        }, 1000)
    }

    override fun postFail(msg: String) {
    }

    override fun followSuccess(bean: CardBean) {
    }

    override fun setListener() {
        super.setListener()
        iv_common_close.setOnClickListener {

            mPresenter.deleteNote(mCardBean.id)

            onBackPressed()
        }

        cancel_back.setOnClickListener {

            mPresenter.deleteNote(mCardBean.id)

            onBackPressed()
        }

        push_right.setOnClickListener {
//            mPresenter.saveCardNode(mCardBean.name,mCardBean.imageUrl,mCardBean.id,mCardBean.content,mCardBean)


            val bundle = Bundle()
            bundle.putString("uploadingText", "正在提交")
            bundle.putString("successText", "审核中")
            bundle.putString("failText", "提交失败")
            mUploadingDialog.arguments = bundle
            mUploadingDialog.show(supportFragmentManager, "")
            Handler().postDelayed({
                mPresenter.saveCardNode(mCardBean.id,1,mCardBean)
            }, 500)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SaveDraftSuccessEvent) {
        onReload(null)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
