package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.widget.OSSDialog
import kotlinx.android.synthetic.main.activity_new_creation_editor.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class NewCreationEditorActivity : MvpActivity<NewCreationEditorPresenter>(), NewCreationEditorContract.View,
        OSSDialog.OSSUploadListener{

    private lateinit var mOSSDialog: OSSDialog
    private var cardId: Int? = null
    private var cardBagId: Int? = null
    private var title: String? = null
    private var imageUrl: String? = null
    private var content: String? = null

    enum class action{
        ACTION_SAVE,
        ACTION_SUBMIT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_creation_editor)
        initData()
        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
        initViewListener()
    }

    private fun initData() {
        cardId = intent.getIntExtra("cardId",0)
        cardBagId = intent.getIntExtra("cardBagId",0)
        title = intent.getStringExtra("title")
        imageUrl = intent.getStringExtra("imageUrl")

        if (cardId == 0){
            cardId = null
        }
        if (cardBagId == 0){
            cardBagId = null
        }
    }

    override fun initStatusBar() {
        if (!Constants.IS_NIGHT) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.black)
                    .fitsSystemWindows(true)
                    .keyboardEnable(true)
                    .init()
        } else {

        }
    }

    override fun createPresenter(): NewCreationEditorPresenter {
        return NewCreationEditorPresenter(this)
    }

    override fun postSuccess() {

    }

    override fun postFail(msg: String?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BaseActivity.RESULT_OK && data != null) {
            var photoUri = data.data
            var imagePath = ""
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
            /*if (text.contains("<p>") && text.indexOf("<p>") != 0) {
                content = "<p>" + text.substring(0, text.indexOf("<p>")) + "</p>" + text.substring(text.indexOf("<p>"))
            } else if (!text.contains("<p>")) {
                content = "<p>$text</p>"
            }*/
            content = text
            tv_toolbar_commit.isEnabled = text.isNotEmpty()
            tv_toolbar_save_note.isEnabled = text.isNotEmpty()
        })
        cb_editor_bold.setOnClickListener {
            editor.setBold()
        }
        iv_editor_album.setOnClickListener {
            // 激活系统图库，选择一张图片
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        cb_editor_night.setOnCheckedChangeListener { _, isChecked ->
            // 设置编辑器是否夜间模式
            editor.setIsEyeshield(isChecked)
        }
        tv_toolbar_commit.setOnClickListener {
            // 提交卡片
            mPresenter.submitReview(cardId,title,imageUrl,cardBagId,content)
        }
        tv_toolbar_save_note.setOnClickListener {
            // 保存为笔记
            mPresenter.saveFreeNode(cardId,title,imageUrl,cardBagId,content)
        }
    }
}
