package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import kotlinx.android.synthetic.main.activity_create_circle_name.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleNameActivity : MvpActivity<CreateCircleNamePresenter>(), CreateCircleNameContract.View {

    val textWatcher1: TextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
//            tv_toolbar_right.isEnabled = true

            tv_check1.text = "${s.toString().length}/8"
            mPresenter.checkName(s.toString())
            tv_check3.visibility = View.GONE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    override fun createPresenter(): CreateCircleNamePresenter {
        return CreateCircleNamePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_circle_name)
        tv_toolbar_right.isEnabled = false
        create_title.addTextChangedListener(textWatcher1)
    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_right.setOnClickListener {
            val intent = Intent()
            intent.putExtra("circle_title",create_title.text.toString().trim())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    override fun checkSuccess() {
        if (create_title.length() in 3..8) {
            tv_toolbar_right.isEnabled = false
            tv_check3.visibility = View.VISIBLE
            tv_check2.visibility = View.GONE
            tv_check4.visibility = View.GONE
        }else{
            tv_check2.visibility = View.GONE
            tv_check3.visibility = View.GONE
            tv_check4.visibility = View.VISIBLE
        }
    }

    override fun checkCanUse() {
        if (create_title.length() in 3..8) {
            tv_toolbar_right.isEnabled = true
            tv_check2.visibility = View.VISIBLE
            tv_check3.visibility = View.GONE
            tv_check4.visibility = View.GONE
        } else {
            tv_toolbar_right.isEnabled = false
            tv_check2.visibility = View.GONE
            tv_check3.visibility = View.GONE
            tv_check4.visibility = View.VISIBLE
        }
    }

    override fun getDataSuccess(bean: CircleBean?) {
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun postFail(msg: String?) {
    }


}