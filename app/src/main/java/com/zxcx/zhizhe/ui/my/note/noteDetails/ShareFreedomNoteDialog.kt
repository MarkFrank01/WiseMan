package com.zxcx.zhizhe.ui.my.note.noteDetails

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.widget.LinearLayout
import butterknife.Unbinder
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
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.WebViewUtils
import com.zxcx.zhizhe.widget.PermissionDialog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.dialog_share_freedom_note.*
import top.zibin.luban.Luban
import java.io.File
import java.util.*

class ShareFreedomNoteDialog : BaseDialog() {

	private var mWebView: WebView? = null
	private var name: String? = null
	private var url: String? = null
	private var date: String? = null
	private val unbinder: Unbinder? = null
	private lateinit var plat: Platform


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		return inflater.inflate(R.layout.dialog_share_freedom_note, container)
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
		name = arguments?.getString("name")
		url = arguments?.getString("url")
		date = bundle?.getString("date")
	}

	private fun initView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// 延迟共享动画的执行
			//postponeEnterTransition();
		}
		tv_dialog_share_title.text = name
		tv_dialog_share_info.setText(getString(R.string.tv_item_card_note_info, date, "记录"))

		//获取WebView，并将WebView高度设为WRAP_CONTENT
		mWebView = WebViewUtils.getWebView(activity)
		val params = LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		mWebView?.layoutParams = params
		fl_dialog_share.addView(mWebView)
		mWebView?.loadUrl(url)
	}

	override fun setListener() {
		super.setListener()
		iv_dialog_share_wechat.setOnClickListener {
			plat = ShareSDK.getPlatform(Wechat.NAME)
			checkPermission(plat.name)
		}
		iv_dialog_share_moments.setOnClickListener {
			plat = ShareSDK.getPlatform(WechatMoments.NAME)
			checkPermission(plat.name)
		}
		iv_dialog_share_qq.setOnClickListener {
			plat = ShareSDK.getPlatform(QQ.NAME)
			checkPermission(plat.name)
		}
		iv_dialog_share_weibo.setOnClickListener {
			plat = ShareSDK.getPlatform(SinaWeibo.NAME)
			checkPermission(plat.name)
		}
		iv_dialog_share_back.setOnClickListener {
			dismiss()
		}
	}

	private fun checkPermission(platform: String) {
		val rxPermissions = RxPermissions(mActivity)
		rxPermissions
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

		mDisposable = Flowable.just<ViewGroup>(sv_dialog_share)
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
						oks.show(mActivity)
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
