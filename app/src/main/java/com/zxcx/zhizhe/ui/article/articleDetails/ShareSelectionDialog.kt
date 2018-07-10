package com.zxcx.zhizhe.ui.article.articleDetails

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import butterknife.OnClick
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.onekeyshare.OnekeyShare
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseDialog
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.PermissionDialog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.dialog_share_card.*
import top.zibin.luban.Luban
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

open class ShareSelectionDialog : BaseDialog() {

	private var mWebView: WebView? = null
	private lateinit var cardBean: CardBean
	private var isReady = false
	private var SETUP_HTML: String = ""
	private var content = ""
	private lateinit var plat: Platform


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		return inflater.inflate(R.layout.dialog_share_card, container)
	}

	override fun onStart() {
		super.onStart()
		val dialog = dialog
		if (dialog != null) {
			val window = getDialog().window
			window?.setBackgroundDrawableResource(R.color.translate)
			window.decorView.setPadding(ScreenUtils.dip2px(10f), ScreenUtils.dip2px(10f),
					ScreenUtils.dip2px(10f), ScreenUtils.dip2px(10f))
			val lp = window.attributes
			lp.gravity = Gravity.CENTER
			lp.width = WindowManager.LayoutParams.MATCH_PARENT
			lp.height = WindowManager.LayoutParams.MATCH_PARENT
			window.attributes = lp
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initData()
		initView()
	}

	override fun onDestroy() {
		if (mWebView != null) {
			mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
			mWebView?.clearHistory()

			(mWebView?.parent as ViewGroup).removeView(mWebView)
			mWebView?.destroy()
			mWebView = null
		}
		super.onDestroy()
	}

	private fun initData() {
		val bundle = arguments
		if (bundle != null) {
			cardBean = bundle.getParcelable("cardBean")
			content = bundle.getString("content")
		}

		val isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false)
		if (isNight) {
			SETUP_HTML = "file:///android_asset/preview_dark.html"
		} else {
			SETUP_HTML = "file:///android_asset/preview_light.html"
		}
	}

	private fun initView() {

		if (!StringUtils.isEmpty(cardBean.name))
			tv_dialog_share_title.text = cardBean.name

		val date = DateTimeUtils.getDateString(cardBean.date)
		if (!StringUtils.isEmpty(date))
			tv_dialog_share_date.text = date

		if (!StringUtils.isEmpty(cardBean.categoryName))
			tv_dialog_share_category.text = cardBean.categoryName

		if (!StringUtils.isEmpty(cardBean.getLabelName()))
			tv_dialog_share_label.text = cardBean.getLabelName()

		if (!StringUtils.isEmpty(cardBean.imageUrl)) {
			val imageUrl = ZhiZheUtils.getHDImageUrl(cardBean.imageUrl)
			ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, iv_dialog_share)
		}

		//获取WebView，并将WebView高度设为WRAP_CONTENT
		mWebView = WebViewUtils.getWebView(activity)
		context?.getColorForKotlin(R.color.bg_details)?.let { mWebView?.setBackgroundColor(it) }
		val params = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		mWebView?.layoutParams = params

		mWebView?.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
				return true
			}

			override fun onPageFinished(view: WebView, url: String) {
				isReady = url.equals(SETUP_HTML)
				iv_dialog_share_qr?.visibility = View.VISIBLE
			}
		}
		fl_dialog_share.addView(mWebView)
		if (!StringUtils.isEmpty(content)) {
			mWebView?.loadUrl(SETUP_HTML)
			if (!StringUtils.isEmpty(content)) {
				try {
					exec("javascript:RE.setBody('" + URLEncoder.encode(content, "UTF-8") + "');")
				} catch (e: UnsupportedEncodingException) {
					// No handling
				}

			}
		}
	}

	@OnClick(R.id.iv_dialog_share_wechat)
	fun onMIvDialogShareWechatClicked() {
		plat = ShareSDK.getPlatform(Wechat.NAME)
		checkPermission(plat.name)
	}

	@OnClick(R.id.iv_dialog_share_moments)
	fun onMIvDialogShareMomentsClicked() {
		plat = ShareSDK.getPlatform(WechatMoments.NAME)
		checkPermission(plat.name)
	}

	@OnClick(R.id.iv_dialog_share_qq)
	fun onMIvDialogShareQqClicked() {
		plat = ShareSDK.getPlatform(QQ.NAME)
		checkPermission(plat.name)
	}

	@OnClick(R.id.iv_dialog_share_weibo)
	fun onMIvDialogShareWeiboClicked() {
		plat = ShareSDK.getPlatform(SinaWeibo.NAME)
		checkPermission(plat.name)
	}

	@OnClick(R.id.iv_dialog_share_back)
	fun onMIvDialogShareBackClicked() {
		dismiss()
	}

	private fun exec(trigger: String) {
		if (isReady) {
			mWebView?.evaluateJavascript(trigger, null)
		} else {
			Handler().postDelayed({ exec(trigger) }, 100)
		}
	}

	private fun checkPermission(platform: String) {
		val rxPermissions = RxPermissions(activity!!)
		val subscribe = rxPermissions
				.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe { permission ->
					if (permission.granted) {
						// `permission.name` is granted !
						showShare(platform)
					} else if (permission.shouldShowRequestPermissionRationale) {
						// Denied permission without ask never again
						toastShow("权限已被拒绝！无法进行操作")
					} else {
						// Denied permission with ask never again
						// Need to go to the settings
						val permissionDialog = PermissionDialog()
						permissionDialog.show(fragmentManager, "")
					}
				}
	}

	private fun showShare(platform: String?) {

		val oks = OnekeyShare()
		//指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
		if (platform != null) {
			oks.setPlatform(platform)
		}

		oks.callback = object : PlatformActionListener {
			override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
				//保持为空，覆盖掉提示
			}

			override fun onError(platform: Platform, i: Int, throwable: Throwable) {

			}

			override fun onCancel(platform: Platform, i: Int) {

			}
		}

		mDisposable = Flowable.just(sv_dialog_share)
				.subscribeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe { subscription -> showLoading() }
				.observeOn(Schedulers.io())
				.map { view ->
					val bitmap = ScreenUtils.getBitmapByView(view)
					val fileName = FileUtil.getRandomImageName()
					FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName)
					val path = FileUtil.PATH_BASE + fileName
					Luban.with(activity)
							.load(File(path))                     //传入要压缩的图片
							.get()
							.path//启动压缩
					//                    return path;
				}
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(object : DisposableSubscriber<String>() {

					override fun onNext(s: String) {
						// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
						oks.setImagePath(s)//确保SDcard下面存在此张图片
						//启动分享
						hideLoading()
						oks.show(activity)
						dismiss()
					}

					override fun onError(t: Throwable) {
						hideLoading()
					}

					override fun onComplete() {

					}
				})
	}
}
