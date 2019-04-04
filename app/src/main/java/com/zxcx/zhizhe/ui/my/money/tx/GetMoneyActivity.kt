package com.zxcx.zhizhe.ui.my.money.tx

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.money.MoneyBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.utils.parseFloat
import kotlinx.android.synthetic.main.activity_get_money.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
//class GetMoneyActivity:BaseActivity() {
class GetMoneyActivity : MvpActivity<GetMoneyPresenter>(), GetMoneyContract.View {

    private var money: String = ""

    val textWatcher1: TextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {

            if (s.toString().trim()!="") {
//                if (Integer.parseInt(s.toString().trim()) < 200.00) {
//                if (s.toString().trim() < "200") {

                    LogCat.e(""+s.toString().trim().parseFloat())
                if (s.toString().trim().parseFloat()<200.00){
                    check_my_money.visibility = View.VISIBLE
                    tv_toolbar_right.isEnabled = false
                    tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))

                } else {
                    check_my_money.visibility = View.GONE
                    tv_toolbar_right.isEnabled = true
                    tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
                    money = s.toString().trim()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_money)

        initView()
    }

    private fun initView() {
        initToolBar("提现")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))

        et_my_money.addTextChangedListener(textWatcher1)
    }

    override fun createPresenter(): GetMoneyPresenter {
        return GetMoneyPresenter(this)
    }

    override fun applyForWithdrawalSuccess() {
        toastShow("申请提现成功")
        finish()
    }

    override fun nomoreMoney(msg: String) {
        toastShow(msg)
    }


    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            mPresenter.applyForWithdrawal(money)
        }
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MoneyBean?) {
    }

}