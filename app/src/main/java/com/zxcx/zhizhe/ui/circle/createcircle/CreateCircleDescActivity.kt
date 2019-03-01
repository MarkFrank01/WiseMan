package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.activity_create_circle_desc.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleDescActivity:BaseActivity() {

    val textWatcher1:TextWatcher = object  :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_circle_desc)
    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_right.setOnClickListener {
            val intent = Intent()
            intent.putExtra("circle_desc",create_title.text.toString().trim())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}