package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.CommonDialog
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import kotlinx.android.synthetic.main.dialog_note_title.*

/**
 * Created by anm on 2017/7/21.
 */

class NewLabelDialog : CommonDialog() {

	lateinit var mListener: (String) -> Unit

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = inflater.inflate(R.layout.dialog_new_label, container)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}

	override fun setListener() {
		tv_dialog_cancel.setOnClickListener {
			this.dismiss()
		}
		tv_dialog_confirm.setOnClickListener {
			mListener.invoke(et_dialog_note_title.text.toString())
			Utils.closeInputMethod(et_dialog_note_title)
			dismiss()
		}
		et_dialog_note_title.afterTextChanged {
			tv_dialog_confirm.isEnabled = it.isNotEmpty()
		}
	}
}
