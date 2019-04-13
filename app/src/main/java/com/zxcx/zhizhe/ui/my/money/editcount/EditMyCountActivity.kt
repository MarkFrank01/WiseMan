package com.zxcx.zhizhe.ui.my.money.editcount

import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_edit_my_count.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class EditMyCountActivity :MvpActivity<EditMyCountPresenter>(),EditMyCountContract.View{

    var text1 = ""
    var text2 = ""
    var text3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_count)
        initToolBar("编辑支付宝")
        tv_toolbar_right.visibility = View.VISIBLE
//        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            text1 = ed_1.text.toString().trim()
            text2 = ed_2.text.toString().trim()
            text3 = ed_3.text.toString().trim()

            if (text1.isNotEmpty()&&text2.isNotEmpty()&&text3.isNotEmpty()){
                mPresenter.bindingAlipay(text1,text2,text3)
            }else{
                toastShow("请填写所有信息")
            }

        }
    }

    override fun createPresenter(): EditMyCountPresenter {
        return EditMyCountPresenter(this)
    }

    override fun bindingAlipaySuccess() {
        toastShow("绑定成功")
        finish()
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: InviteBean?) {
    }
}