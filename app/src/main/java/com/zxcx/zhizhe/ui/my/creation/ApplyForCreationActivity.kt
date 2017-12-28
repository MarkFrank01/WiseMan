package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_apply_for_creation.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.regex.Pattern

/**
 * 申请创作
 */
class ApplyForCreationActivity : BaseActivity() , INullPostPresenter{

    private var phoneRules = "^1\\d{10}$"
    private var nameRules = "^[\\u4e00-\\u9fa5]*\$"
    private var idCardRules = "^\\d{17}([0-9]|X)\$"
    private var phonePattern = Pattern.compile(phoneRules)
    private var namePattern = Pattern.compile(nameRules)
    private var idCardPattern = Pattern.compile(idCardRules)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_for_creation)
        initToolBar("申请创作")
        initView()
    }

    private fun initView() {
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.color_text_enable_blue))
        tv_toolbar_right.isEnabled = false

        et_apply_name.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_toolbar_right.isEnabled = check()
            }
        })

        et_apply_phone.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_toolbar_right.isEnabled = check()
            }
        })

        et_apply_id_card.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_toolbar_right.isEnabled = check()
            }
        })

        tv_toolbar_right.setOnClickListener {
            applyCreation(et_apply_name.text.toString(),et_apply_phone.text.toString(),et_apply_id_card.text.toString())
        }
    }

    private fun applyCreation(name: String, phone: String, idCard: String) {
        mDisposable = AppClient.getAPIService().applyCreation(name, phone, idCard)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(this) {

                    override fun onNext(baseBean: BaseBean<*>) {
                        postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    override fun postSuccess() {
        toastShow("申请成功")
        Utils.closeInputMethod(mActivity)
        finish()
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    private fun check(): Boolean {
        return checkName()&&checkPhone()&&checkIdCard()
    }

    private fun checkName(): Boolean {
        return namePattern.matcher(et_apply_name.text.toString()).matches()
    }

    private fun checkPhone(): Boolean {
        return phonePattern.matcher(et_apply_phone.text.toString()).matches()
    }

    private fun checkIdCard(): Boolean {
        return idCardPattern.matcher(et_apply_id_card.text.toString()).matches()
    }

}
