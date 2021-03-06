package com.zxcx.zhizhe.ui.my.feedback.feedback

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * 反馈页面
 */

class FeedbackActivity : MvpActivity<FeedbackPresenter>(), FeedbackContract.View {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_feedback)

		initToolBar()
		tv_toolbar_right.visibility = View.VISIBLE
		tv_toolbar_right.text = "完成"
		tv_toolbar_right.isEnabled = false
		tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))
	}

	override fun onDestroy() {
		Utils.closeInputMethod(this)
		super.onDestroy()
	}

	override fun createPresenter(): FeedbackPresenter {
		return FeedbackPresenter(this)
	}

	override fun postSuccess() {
		toastShow("反馈已提交，感谢你的支持")
		finish()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}

	override fun setListener() {

		tv_toolbar_right.setOnClickListener {
			val content = et_feedback_content.text.toString()
			val appType = Constants.APP_TYPE
			val appChannel = WalleChannelReader.getChannel(this)
			val appVersion = Utils.getAppVersionName(this)
			mPresenter.feedback(content, appType, appChannel, appVersion)
		}

		et_feedback_content.afterTextChanged {
			tv_toolbar_right.isEnabled = it.length >= 10
			tv_feedback_residue.text = getString(R.string.tv_feedback_residue, it.length, 600 - it.length)
			if (it.length == 600) {
				tv_feedback_residue.setTextColor(mActivity.getColorForKotlin(R.color.red))
			} else {
				tv_feedback_residue.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
			}
		}

		et_feedback_content.setOnEditorActionListener { v, actionId, event ->
			//此处会响应2次 分别为ACTION_DOWN和ACTION_UP
			if (actionId == EditorInfo.IME_ACTION_SEARCH
					|| actionId == EditorInfo.IME_ACTION_DONE
					|| event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {

				Utils.closeInputMethod(et_feedback_content)

				return@setOnEditorActionListener true
			}
			return@setOnEditorActionListener false
		}

	}
}
