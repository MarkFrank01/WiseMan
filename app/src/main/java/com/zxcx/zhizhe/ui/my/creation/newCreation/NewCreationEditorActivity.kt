package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SaveFreedomNoteSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.note.NoteActivity
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_new_creation_editor.*
import org.greenrobot.eventbus.EventBus
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class NewCreationEditorActivity : MvpActivity<NewCreationEditorPresenter>(), NewCreationEditorContract.View,
        OSSDialog.OSSUploadListener{

    private lateinit var mOSSDialog: OSSDialog
    private var cardId: Int = 0
    private var cardBagId: Int = 0
    private var title: String = ""
    private var imageUrl: String = ""
    private var content: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_creation_editor)
        initData()
        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
        initViewListener()
    }

    override fun onBackPressed() {
        Utils.closeInputMethod(mActivity)
        super.onBackPressed()
    }

    private fun initData() {
        cardId = intent.getIntExtra("cardId",0)
        cardBagId = intent.getIntExtra("cardBagId",0)
        title = intent.getStringExtra("title")
        imageUrl = intent.getStringExtra("imageUrl")
        if (cardId != 0){
            editor.setCardId(cardId)
        }
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
                .keyboardEnable(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.background)
                .statusBarDarkFont(true, 0.2f)
                .flymeOSStatusBarFontColor(R.color.text_color_1)
        mImmersionBar.init()
    }

    override fun createPresenter(): NewCreationEditorPresenter {
        return NewCreationEditorPresenter(this)
    }

    override fun postSuccess() {
        EventBus.getDefault().post(SaveFreedomNoteSuccessEvent())
        val intent = Intent(mActivity,CreationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        toastShow("提交成功")
        finish()
    }

    override fun saveFreedomNoteSuccess() {
        EventBus.getDefault().post(SaveFreedomNoteSuccessEvent())
        val intent = Intent(mActivity,NoteActivity::class.java)
        intent.putExtra("isFreedomNote",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        toastShow("保存成功")
        finish()
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
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
        cb_editor_bold.isChecked = false
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
            editor.setBold(cb_editor_bold.isChecked)
        }
        iv_editor_album.setOnClickListener {
            // 激活系统图库，选择一张图片
            val rxPermissions = RxPermissions(this)
            rxPermissions
                    .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { permission ->
                        when {
                            permission.granted -> {
                                // `permission.name` is granted !
                                // 激活系统图库，选择一张图片
                                val intent = Intent(Intent.ACTION_PICK)
                                intent.type = "image/*"
                                startActivityForResult(intent, 0)
                            }
                            permission.shouldShowRequestPermissionRationale -> // Denied permission without ask never again
                                toastShow("权限已被拒绝！无法进行操作")
                            else -> {
                                // Denied permission with ask never again
                                // Need to go to the settings
                                val permissionDialog = PermissionDialog()
                                permissionDialog.show(fragmentManager, "")
                            }
                        }
                    }
        }
        cb_editor_eyeshield.setOnCheckedChangeListener { _, isChecked ->
            // 设置编辑器是否夜间模式
            editor.setIsEyeshield(isChecked)
            if (isChecked){
                ll_rte.setBackgroundColor(Color.parseColor("#F0EFE4"))
                ll_rte_bottom.setBackgroundColor(Color.parseColor("#FDFAEF"))
                mImmersionBar
                        .statusBarColor("#F0EFE4")
                        .init()
            }else{
                ll_rte.setBackgroundResource(R.color.background)
                ll_rte_bottom.setBackgroundResource(R.color.white)
                initStatusBar()
            }
        }
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
        tv_toolbar_commit.setOnClickListener {
            // 提交卡片
            mPresenter.submitReview(cardId,title,imageUrl,cardBagId,content)
        }
        tv_toolbar_save_note.setOnClickListener {
            // 保存为笔记
            mPresenter.saveFreeNode(cardId,title,imageUrl,cardBagId,content)
        }

        //添加方法给js调用
        editor.addJavascriptInterface(this,"native")
    }

    @JavascriptInterface
    fun setBold(boolean: Boolean){
        runOnUiThread{
            cb_editor_bold.isChecked = boolean
        }
    }
}
