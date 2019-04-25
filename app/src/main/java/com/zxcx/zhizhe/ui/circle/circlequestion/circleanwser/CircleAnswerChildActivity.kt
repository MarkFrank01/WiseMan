package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_circle_answer_child.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerChildActivity : MvpActivity<CircleAnswerChildPresenter>(), CircleAnswerChildContract.View {

    val textWatcher1: TextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {

            if (question_desc.text.toString().length > 1) {
                tv_toolbar_right.isEnabled = true
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private var titleName = ""

    //圈子Id
    private var circleId: Int = 0

    //qaId
    private var qaId: Int = 0

    //qaCommentId
    private var qaCommentId:Int = 0

    //回答
    private var description: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_answer_child)

        initData()
        initView()
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            description = question_desc.text.toString().trim()

            mPresenter.createAnswer(circleId, qaId, qaCommentId, description)
        }

        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun createPresenter(): CircleAnswerChildPresenter {
        return CircleAnswerChildPresenter(this)
    }

    override fun createAnswerSuccess() {
        hideKB()
        toastShow("回复成功")
        onBackPressed()
    }

    override fun postSuccess(bean: QuestionBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: QuestionBean?) {
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            //延迟弹出键盘
            Handler().postDelayed({Utils.showInputMethod(question_desc)},100)
        }
    }

    private fun initData(){
        qaId = intent.getIntExtra("qaId",0)
        circleId = intent.getIntExtra("CircleId",0)
        titleName = intent.getStringExtra("name")
        qaCommentId = intent.getIntExtra("qaCommentId",0)
    }

    private fun initView(){
        question_desc.addTextChangedListener(textWatcher1)
        tv_toolbar_right.isEnabled = false
        tv_toolbar_title.text = "回复：$titleName"
    }

    private fun hideKB(){
        var imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive&&currentFocus!=null){
            imm.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}