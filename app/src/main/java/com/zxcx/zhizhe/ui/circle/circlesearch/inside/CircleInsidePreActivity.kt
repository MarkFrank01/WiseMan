package com.zxcx.zhizhe.ui.circle.circlesearch.inside

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_circle_search_pre.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleInsidePreActivity :BaseActivity(){

    private var circleId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_search_pre)
        initData()
    }

    override fun setListener() {
        et_search.setOnEditorActionListener(SearchListener())

        tv_search_cancel.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        Utils.closeInputMethod(et_search)
        super.onBackPressed()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //延迟弹出软键盘
            Handler().postDelayed({ Utils.showInputMethod(et_search) }, 100)
        }
    }

    override fun onResume() {
        et_search.setSelection(et_search.length())
        super.onResume()
    }

    fun initData(){
        circleId = intent.getIntExtra("circleId",0)
    }

    fun gotoSearchResult(keyword: String) {
        Utils.closeInputMethod(et_search)
        val intent = Intent(this, CircleInsideActivity::class.java)
        intent.putExtra("keyword",keyword)
        intent.putExtra("circleId",circleId)
        startActivity(intent)
        finish()
    }

    internal inner class SearchListener : TextView.OnEditorActionListener {

        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {

                if (StringUtils.isEmpty(v.text.toString())){
                    toastShow("搜索内容不能为空！")
                    return true
                }else{
                    gotoSearchResult(v.text.toString())
                    return true
                }

                return true
            }
            return false
        }
    }
}