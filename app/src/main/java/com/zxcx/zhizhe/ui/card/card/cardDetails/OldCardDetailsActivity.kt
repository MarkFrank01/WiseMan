package com.zxcx.zhizhe.ui.card.card.cardDetails

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CollectSuccessEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.collect.SelectCollectFolderActivity
import com.zxcx.zhizhe.ui.card.card.share.DiyShareActivity
import com.zxcx.zhizhe.ui.card.card.share.ShareCardDialog
import com.zxcx.zhizhe.ui.card.card.share.ShareWayDialog
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.ScreenUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

fun getSB(): String {
    return "sb"
}

class OldCardDetailsActivity : MvpActivity<OldCardDetailsPresenter>(), OldCardDetailsContract.View, ShareWayDialog.DefaultShareDialogListener {

    @BindView(R.id.tv_card_details_name)
    internal var mTvCardDetailsName: TextView? = null
    @BindView(R.id.tv_card_details_subhead)
    private var mTvCardDetailsSubhead: TextView? = null
    @BindView(R.id.tv_card_details_content)
    internal var mTvCardDetailsContent: TextView? = null
    @BindView(R.id.cb_card_details_collect)
    internal var mCbCardDetailsCollect: CheckBox? = null
    @BindView(R.id.cb_card_details_like)
    internal var mCbCardDetailsLike: CheckBox? = null
    @BindView(R.id.iv_card_details)
    private var mIvCardDetails: ImageView? = null

    @BindView(R.id.scv_card_details)
    private var mScvCardDetails: NestedScrollView? = null
    @BindView(R.id.tv_card_details_title)
    internal var mTvCardDetailsTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)

        val tp = mTvCardDetailsSubhead!!.paint
        tp.isFakeBoldText = true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //            TextViewUtils.adjustTvTextSize(mTvCardDetailsContent);
            val para = mIvCardDetails?.layoutParams
            para?.height = mIvCardDetails!!.width * 3 / 4
            mIvCardDetails?.layoutParams = para
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun createPresenter(): OldCardDetailsPresenter {
        return OldCardDetailsPresenter(this)
    }

    override fun getDataSuccess(bean: OldCardDetailsBean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CollectSuccessEvent) {
        mCbCardDetailsCollect!!.isChecked = true
    }

    @OnClick(R.id.tv_card_details_share)
    fun onShareClicked() {
        val shareWayDialog = ShareWayDialog()
        shareWayDialog.setListener(this)
        shareWayDialog.show(fragmentManager, "")
    }

    @OnClick(R.id.cb_card_details_collect)
    public fun onCollectClicked() {
        if (mCbCardDetailsCollect!!.isChecked) {
            val intent = Intent(mActivity, SelectCollectFolderActivity::class.java)
            startActivity(intent)
            mCbCardDetailsCollect?.isChecked = false
        }
    }

    @OnClick(R.id.iv_card_details_back)
    fun onBackClicked() {
        finish()
    }

    override fun onDefaultShareClick() {
        val bitmap = ScreenUtils.getBitmapByView(mScvCardDetails)
        val fileName = FileUtil.getRandomImageName()
        val imagePath = FileUtil.PATH_BASE + fileName
        FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName)

        val shareCardDialog = ShareCardDialog()
        val bundle = Bundle()
        bundle.putString("imagePath", imagePath)
        shareCardDialog.arguments = bundle
        shareCardDialog.show(fragmentManager, "")
    }

    override fun onDiyShareClick() {
        val intent = Intent(this, DiyShareActivity::class.java)
        startActivity(intent)
    }
}
