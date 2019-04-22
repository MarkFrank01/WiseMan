package com.zxcx.zhizhe.ui.my.money.editcount

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_edit_my_count.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.regex.Pattern

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class EditMyCountActivity :MvpActivity<EditMyCountPresenter>(),EditMyCountContract.View{

    private var phoneRules = "^1\\d{10}$"
    private var phonePattern = Pattern.compile(phoneRules)

    var text1 = ""
    var text2 = ""
    var text3 = ""

    var checkIN1 = false
    var checkIN2 = false
    var checkIN3 = false

    val textWatcher1:TextWatcher = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            checkIN1 = s.toString().trim()!=""
            if (checkIN1&&checkIN2&&checkIN3){
                tv_toolbar_right.isEnabled = true
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))

            }else{
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
                tv_toolbar_right.isEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    val textWatcher2:TextWatcher = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            checkIN2 = s.toString().trim()!=""
            if (checkIN1&&checkIN2&&checkIN3){
                tv_toolbar_right.isEnabled = true
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))

            }else{
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
                tv_toolbar_right.isEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    val textWatcher3:TextWatcher = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
//            checkIN3 = s.toString().trim()!=""
            checkIN3 = checkPhoe()
            if (checkIN1&&checkIN2&&checkIN3){
                tv_toolbar_right.isEnabled = true
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))

            }else{
                tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
                tv_toolbar_right.isEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_count)
        initToolBar("编辑支付宝")
        tv_toolbar_right.visibility = View.VISIBLE
//        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
        tv_toolbar_right.isEnabled  = false

        ed_1.addTextChangedListener(textWatcher1)
        ed_2.addTextChangedListener(textWatcher2)
        ed_3.addTextChangedListener(textWatcher3)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            //延迟弹出软键盘
            Handler().postDelayed({Utils.showInputMethod(ed_1)},100)
        }
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

    private fun checkPhoe():Boolean{
        return phonePattern.matcher(ed_3.text.toString().trim()).matches()
    }
}