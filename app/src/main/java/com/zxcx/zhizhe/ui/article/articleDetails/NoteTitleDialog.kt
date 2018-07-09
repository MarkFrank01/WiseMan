package com.zxcx.zhizhe.ui.article.articleDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SaveCardNoteSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.CommonDialog
import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.utils.afterTextChanged
import kotlinx.android.synthetic.main.dialog_note_title.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by anm on 2017/7/21.
 */

class NoteTitleDialog : CommonDialog(), INullPostPresenter {


	private var withCardId: Int = 0
	private var title: String? = null
	private var imageUrl: String? = null
	private var content: String? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = inflater.inflate(R.layout.dialog_note_title, container)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initData()
		et_dialog_note_title.setText(title)
		et_dialog_note_title.setSelection(et_dialog_note_title.length())
	}

	override fun setListener() {
		tv_dialog_cancel.setOnClickListener {
			this.dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			// 保存笔记
			saveCardNode(et_dialog_note_title.getText().toString(), imageUrl, withCardId, content)
		}
		et_dialog_note_title.afterTextChanged {
			tv_dialog_confirm.isEnabled = it.isNotEmpty()
		}
	}

	private fun initData() {
		val bundle = arguments
		if (bundle != null) {
			withCardId = bundle.getInt("withCardId", 0)
			title = bundle.getString("title")
			imageUrl = bundle.getString("imageUrl")
			content = bundle.getString("content")
		}
	}

	private fun saveCardNode(title: String, imageUrl: String?, withCardId: Int, content: String?) {
		mDisposable = AppClient.getAPIService().saveCardNode(null, title, imageUrl, withCardId, content)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main_loading(this))
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(this) {
					override fun onNext(bean: BaseBean<*>) {
						postSuccess()
					}
				})
		addSubscription(mDisposable)
	}

	override fun postSuccess() {
		EventBus.getDefault().post(SaveCardNoteSuccessEvent())
		dismiss()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
		dismiss()
	}
}
