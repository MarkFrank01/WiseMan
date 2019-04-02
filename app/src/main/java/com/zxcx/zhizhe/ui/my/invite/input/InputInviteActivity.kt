package com.zxcx.zhizhe.ui.my.invite.input

import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_input_invite.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class InputInviteActivity: MvpActivity<InputInvitePresenter>(),InputInviteContract.View {


    private var pushCode :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_invite)

        initToolBar("填写邀请码")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.isEnabled = false

        vci_invite.focus()
    }


    override fun setListener() {
        vci_invite.setOnCompleteListener {
//            toastShow(it)
            tv_toolbar_right.isEnabled = true
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
            pushCode = it
        }

        tv_toolbar_right.setOnClickListener {
            mPresenter.inputInvitationCode(pushCode)
        }
    }

    override fun createPresenter(): InputInvitePresenter {
        return InputInvitePresenter(this)
    }

    override fun inputInvitationCodeSuccess(bean: InviteBean) {
        toastShow("填写完成")
        finish()
    }

    override fun getDataSuccess(bean: InviteBean?) {
    }

    override fun errormsg(msg: String) {
        toastShow("邀请码不正确")
    }
}