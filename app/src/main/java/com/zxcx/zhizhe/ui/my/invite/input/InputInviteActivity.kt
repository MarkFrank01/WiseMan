package com.zxcx.zhizhe.ui.my.invite.input

import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.activity_input_invite.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class InputInviteActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_invite)

        initToolBar("填写邀请码")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"

        vci_invite.focus()
    }


    override fun setListener() {
        vci_invite.setOnCompleteListener {
            toastShow(it)
        }
    }
}