package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_creation_editor.*
import org.greenrobot.eventbus.EventBus

class CreationPreviewActivity : BaseActivity() {

	private var id = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creation_editor)

		initEditor()
		tv_toolbar_right.isEnabled = true
		ll_creation_editor_bottom.visibility = View.GONE
	}

	private fun initEditor() {
		id = intent.getStringExtra("id")
		val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.creation_preview_url) + id
//		val url = "http://192.168.1.153:8043/pages/card-editor.html"
		editor.url = url
		val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
		editor.setTimeStampAndToken(token)
	}

	override fun setListener() {
		tv_toolbar_back.setOnClickListener {
			onBackPressed()
		}

		tv_toolbar_right.setOnClickListener {
			editor.saveNote()
		}

		//添加方法给js调用
		editor.addJavascriptInterface(this, "native")
	}

	override fun initStatusBar() {
		mImmersionBar = ImmersionBar.with(this)
		if (!Constants.IS_NIGHT) {
			mImmersionBar
					.statusBarColor(R.color.background)
					.statusBarDarkFont(true, 0.2f)
					.flymeOSStatusBarFontColor(R.color.text_color_1)
					.keyboardEnable(true)
					.fitsSystemWindows(true)
		} else {
			mImmersionBar
					.statusBarColor(R.color.background)
					.flymeOSStatusBarFontColor(R.color.text_color_1)
					.keyboardEnable(true)
					.fitsSystemWindows(true)
		}
		mImmersionBar.init()
	}

	@JavascriptInterface
	fun commitSuccess() {
		toastShow("提交审核成功")
		EventBus.getDefault().post(CommitCardReviewEvent())
		startActivity(CreationActivity::class.java) {
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			it.putExtra("goto", 1)
		}
		finish()
	}

	@JavascriptInterface
	fun commitFail() {
		toastError("提交审核失败")
	}
}
