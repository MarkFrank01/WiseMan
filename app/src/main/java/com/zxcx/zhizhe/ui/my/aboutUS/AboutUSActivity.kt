package com.zxcx.zhizhe.ui.my.aboutUS

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.share.ShareDialog
import com.zxcx.zhizhe.ui.my.feedback.feedback.FeedbackActivity
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * 关于我们页面
 */

class AboutUSActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_about_us)
		initToolBar()
	}

	override fun initStatusBar() {

	}

	override fun setListener() {

		tv_about_us_evaluate.setOnClickListener {
			try {
				val uri = Uri.parse("market://details?id=$packageName")
				val intent = Intent(Intent.ACTION_VIEW, uri)
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
			} catch (e: Exception) {
				toastShow("您没有安装应用市场")
			}
		}

		tv_about_share.setOnClickListener {
			val shareCardDialog = ShareDialog()
			val bundle = Bundle()
			bundle.putString("title", getString(R.string.app_name))
			bundle.putString("text", "只看实用知识")
			bundle.putString("url", "http://a.app.qq.com/o/simple.jsp?pkgname=com.zxcx.zhizhe")
			bundle.putString("imageUrl", "http://zhizhe-prod.oss-cn-shenzhen.aliyuncs.com/Icon_1024.png")
			shareCardDialog.arguments = bundle
			shareCardDialog.show(supportFragmentManager, "")
		}

		tv_about_us_feedback.setOnClickListener {
			mActivity.startActivity(FeedbackActivity::class.java) {}
		}
	}
}
