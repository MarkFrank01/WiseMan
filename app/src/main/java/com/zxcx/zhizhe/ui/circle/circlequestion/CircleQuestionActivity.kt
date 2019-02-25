package com.zxcx.zhizhe.ui.circle.circlequestion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.GlideEngine
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import kotlinx.android.synthetic.main.activity_circle_question.*

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionActivity:MvpActivity<CircleQuestionPresenter>(),CircleQuestionContract.View,
        OSSDialog.OSSUploadListener,GetPicBottomDialog.GetPicDialogListener{

    private lateinit var mOSSDialog: OSSDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_question)

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
    }

    override fun setListener() {
        ll_add_photo.setOnClickListener {
            EasyPhotos.createAlbum(this,false, GlideEngine.getInstance())
                    .setCount(6)
                    .start(101)
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode){
            //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
            val resultPhotos = data.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)

            //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
            val resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS)

            for (res in resultPaths){
                LogCat.e("my photo is $res")
            }
        }
    }
}