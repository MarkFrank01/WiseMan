package com.zxcx.zhizhe.ui.my.money

import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.money.editcount.EditMyCountActivity
import com.zxcx.zhizhe.ui.my.money.tx.GetMoneyActivity
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_my_money.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MyMoneyActivity :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_money)

        initToolBar("我的账户")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "账单"
    }

    override fun setListener() {
        //去提现
        goto_tx.setOnClickListener {
            mActivity.startActivity(GetMoneyActivity::class.java){}
        }

        //去编辑支付宝
        load_more_load_end_view.setOnClickListener {
            mActivity.startActivity(EditMyCountActivity::class.java){}
        }
    }
}