package com.zxcx.zhizhe.ui.my.pastelink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.CommonDialog
import kotlinx.android.synthetic.main.dialog_single.*

class DeleteLinkDialog:CommonDialog(){
    lateinit var mConfirmListener: () -> Unit
    lateinit var mCancelListener: () -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_single, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_dialog_title.text = "是否删除作品链接"
    }

    override fun setListener() {
        super.setListener()
        tv_dialog_cancel.setOnClickListener {
            mCancelListener.invoke()
            this.dismiss()
        }
        tv_dialog_confirm.setOnClickListener {
            mConfirmListener.invoke()
            this.dismiss()
        }
    }
}