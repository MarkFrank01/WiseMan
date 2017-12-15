package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.widget.OSSDialog
import kotlinx.android.synthetic.main.activity_new_creation_editor.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class NewCreationEditorActivity : MvpActivity<NewCreationEditorPresenter>(), NewCreationEditorContract.View,
        OSSDialog.OSSUploadListener{

    private lateinit var mOSSDialog: OSSDialog
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_creation_editor)

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
        initViewListener()
    }

    override fun createPresenter(): NewCreationEditorPresenter {
        return NewCreationEditorPresenter(this)
    }

    override fun getDataSuccess(bean: NewCreationEditorBean) {

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BaseActivity.RESULT_OK) {
            var photoUri = data.data
            var imagePath :String = ""
            if (photoUri != null) {
                imagePath = FileUtil.getRealFilePathFromUri(mActivity,photoUri)
            } else {
                val extras = data.extras
                if (extras != null) {
                    val imageBitmap = extras.get("data") as Bitmap
                    photoUri = Uri
                            .parse(MediaStore.Images.Media.insertImage(
                                    mActivity.contentResolver, imageBitmap, null, null))

                    imagePath = FileUtil.getRealFilePathFromUri(mActivity,photoUri)
                }
            }
            Luban.with(mActivity)
                    .load(File(imagePath))                     //传入要压缩的图片
                    .setCompressListener(object : OnCompressListener { //设置回调
                        override fun onStart() {
                            // 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        override fun onSuccess(file: File) {
                            //  压缩成功后调用，返回压缩后的图片文件
                            uploadImageToOSS(file.path)
                        }

                        override fun onError(e: Throwable) {
                            //  当压缩过程出现问题时调用
                        }
                    }).launch()    //启动压缩
        }
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(fragmentManager, "")
    }

    override fun uploadSuccess(url: String) {
        editor.insertImage(url)
    }

    private fun initViewListener() {
        editor.setOnTextChangeListener({ text ->
            if (text.contains("<p>") && text.indexOf("<p>") != 0) {
                content = "<p>" + text.substring(0, text.indexOf("<p>")) + "</p>" + text.substring(text.indexOf("<p>"))
            } else if (!text.contains("<p>")) {
                content = "<p>$text</p>"
            }
        })
        cb_editor_bold.setOnClickListener {
            editor.setBold()
        }
        cb_editor_night.setOnCheckedChangeListener { _, isChecked ->
            //todo 设置编辑器是否夜间模式
        }
        tv_toolbar_commit.setOnClickListener {
            //todo 提交卡片
        }
        tv_toolbar_save_note.setOnClickListener {
            //todo 保存为笔记
        }
    }
}
