package com.zxcx.zhizhe.ui.circle.circlesearch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
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
class CircleSearchPreActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_search_pre)
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

    override fun onClick(v: View?) {
        toastShow((v as TextView).text.toString())
    }

    fun gotoSearchResult(keyword: String) {
//        toastShow(keyword + "???")
        Utils.closeInputMethod(et_search)
        val intent = Intent(this,CircleSearchActivity::class.java)
        intent.putExtra("keyword",keyword)
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