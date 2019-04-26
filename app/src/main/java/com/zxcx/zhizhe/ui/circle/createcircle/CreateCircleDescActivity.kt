package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_create_circle_desc.*



/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleDescActivity:BaseActivity() {

    val textWatcher1:TextWatcher = object  :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            tv_check1.text = "${s.toString().length}/300"
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_circle_desc)
        create_title.addTextChangedListener(textWatcher1)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            //延迟弹出软键盘
            Handler().postDelayed({ Utils.showInputMethod(create_title)},100)
        }
    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_right.setOnClickListener {
            hideKB()
            val intent = Intent()
            intent.putExtra("circle_desc",create_title.text.toString().trim())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    private fun hideKB(){
        var imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive&&currentFocus!=null){
            imm.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}