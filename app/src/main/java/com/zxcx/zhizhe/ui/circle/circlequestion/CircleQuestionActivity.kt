package com.zxcx.zhizhe.ui.circle.circlequestion

import android.net.Uri
import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionActivity:MvpActivity<CircleQuestionPresenter>(),CircleQuestionContract.View,
        OSSDialog.OSSUploadListener,GetPicBottomDialog.GetPicDialogListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_question)
    }

    override fun createPresenter(): CircleQuestionPresenter {
        return CircleQuestionPresenter(this)
    }

    override fun pushQuestionSuccess() {
    }

    override fun postSuccess(bean: QuestionBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: QuestionBean?) {
    }

    override fun uploadSuccess(url: String?) {
    }

    override fun uploadFail(message: String?) {
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType?, uri: Uri?, imagePath: String?) {
    }

}