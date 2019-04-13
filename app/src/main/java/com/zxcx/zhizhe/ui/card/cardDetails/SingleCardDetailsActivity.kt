package com.zxcx.zhizhe.ui.card.cardDetails

import android.Manifest
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.load.MultiTransformation
import com.gyf.barlibrary.ImmersionBar
import com.pixplicity.htmlcompat.HtmlCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.label.LabelActivity
import com.zxcx.zhizhe.ui.comment.CommentFragment
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.utils.GlideOptions.bitmapTransform
import com.zxcx.zhizhe.widget.GoodView
import com.zxcx.zhizhe.widget.PermissionDialog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_single_card_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import java.io.File

/**
 * 单个卡片详情页面
 */

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
        if (intent.getBooleanExtra("gotoComment", false)) {
            iv_item_card_details_comment.performClick()
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
            tv_item_card_details_label2.visibility = View.VISIBLE
            tv_item_card_details_label2.text = mCardBean.getSecondLabelName()
        }
        tv_item_card_details_author.text = mCardBean.authorName
        tv_item_card_details_goto_ad.text = mCardBean.authorName
        tv_item_card_details_comment.text = mCardBean.commentNum.toString()
        tv_item_card_details_collect.text = mCardBean.collectNum.toString()
        tv_item_card_details_like.text = mCardBean.likeNum.toString()
        cb_item_card_details_follow.isChecked = mCardBean.isFollow
        cb_item_card_details_collect.isChecked = mCardBean.isCollect
        cb_item_card_details_like.isChecked = mCardBean.isLike
        tv_item_card_details_collect.isEnabled = mCardBean.isCollect
        tv_item_card_details_like.isEnabled = mCardBean.isLike

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
                ColorFilterTransformation(getColorForKotlin(R.color.bg_card_details)))
        GlideApp
                .with(mActivity)
                .load(imageUrl)
                .apply(bitmapTransform(multi))
                .into(iv_card_details_bg)


        val fromHtml = HtmlCompat.fromHtml(mActivity, mCardBean.content, 0)
        tv_item_card_details_content.movementMethod = LinkMovementMethod.getInstance()
        tv_item_card_details_content.text = fromHtml

        tv_item_card_details_time.text = mCardBean.distanceTime
    }

    override fun createPresenter(): CardDetailsPresenter {
        return CardDetailsPresenter(this)
    }

    override fun getDataSuccess(bean: CardBean) {
        mCardBean = bean
        refreshView()
    }

    override fun postSuccess(bean: CardBean) {
        mCardBean = bean
        refreshView()
    }

    override fun postFail(msg: String) {
        toastShow(msg)
    }

    override fun followSuccess(bean: CardBean) {
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnFollowConfirmEvent) {
        //取消关注
        mPresenter.setUserFollow(mCardBean.authorId, 1, mCardBean)
    }

    override fun setListener() {
        super.setListener()
        iv_common_close.setOnClickListener { onBackPressed() }
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
            setFollow(it, mCardBean)
        }
        iv_item_card_details_comment.setOnClickListener {
            commentFragment = CommentFragment()
            val bundle = Bundle()
            bundle.putInt("cardId", mCardBean.id)
            commentFragment?.arguments = bundle

            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_card_details, commentFragment).commitAllowingStateLoss()
        }
        cb_item_card_details_collect.setOnClickListener {
            setCollect(it, mCardBean)
        }
        cb_item_card_details_like.setOnClickListener {
            setLike(it, mCardBean)
        }
        iv_item_card_details_share.setOnClickListener {
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

    private fun setLike(view: View, bean: CardBean) {
        view as CheckBox
        if (mUserId == bean.authorId) {
            toastShow("不能点赞自己哦")
            view.isChecked = !view.isChecked
            return
        }
        if (checkLogin()) {
            bean.isLike = !bean.isLike
            tv_item_card_details_like.isEnabled = mCardBean.isLike
            if (view.isChecked) {
                val goodView = GoodView(this)
                goodView.setTextColor(getColorForKotlin(R.color.button_blue))
                goodView.setText("+1")
                goodView.show(cb_item_card_details_like)
                bean.likeNum += 1
                tv_item_card_details_like.text = bean.likeNum.toString()
                mPresenter.likeCard(bean.id)
            } else {
                val goodView = GoodView(this)
                goodView.setTextColor(getColorForKotlin(R.color.button_blue))
                goodView.setText("-1")
                goodView.show(cb_item_card_details_like)
                bean.likeNum -= 1
                tv_item_card_details_like.text = bean.likeNum.toString()
                mPresenter.removeLikeCard(bean.id)
            }
        } else {
            view.isChecked = !view.isChecked
        }
    }

    private fun setCollect(view: View, bean: CardBean) {
        view as CheckBox
        if (mUserId == bean.authorId) {
            toastShow("不能收藏自己哦")
            view.isChecked = !view.isChecked
            return
        }
        if (checkLogin()) {
            bean.isCollect = !bean.isCollect
            tv_item_card_details_collect.isEnabled = mCardBean.isCollect
            if (view.isChecked) {
                val goodView = GoodView(this)
                goodView.setTextColor(getColorForKotlin(R.color.button_blue))
                goodView.setText("+1")
                goodView.show(view)
                bean.collectNum += 1
                tv_item_card_details_collect.text = bean.collectNum.toString()
                mPresenter.addCollectCard(bean.id)
            } else {
                val goodView = GoodView(this)
                goodView.setTextColor(getColorForKotlin(R.color.button_blue))
                goodView.setText("-1")
                goodView.show(view)
                bean.collectNum -= 1
                tv_item_card_details_collect.text = bean.collectNum.toString()
                mPresenter.removeCollectCard(bean.id)
            }
        } else {
            view.isChecked = !view.isChecked
        }
    }

    private fun setFollow(view: View, bean: CardBean) {
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
            view.isChecked = true
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

    private fun gotoImageShare() {
        mDisposable = Flowable.just(fl_card_details)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { subscription -> showLoading() }
                .map {
                    iv_share_qr.visibility = View.VISIBLE
                    iv_share_qr_bg.visibility = View.VISIBLE
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
                    iv_share_qr_bg.visibility = View.GONE
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
}
