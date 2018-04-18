package com.zxcx.zhizhe.ui.my.note.newNote

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ActionMode
import android.view.MenuItem
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_creation_editor.*

class NoteEditorActivity : BaseActivity(),
        OSSDialog.OSSUploadListener , GetPicBottomDialog.GetPicDialogListener{

    private lateinit var mOSSDialog: OSSDialog

    private var mAction = 0 //0选择标题图，1选择内容图
    private var mActionMode: ActionMode? = null
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_editor)

        initEditor()

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
    }

    private fun initEditor() {
//        val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.note_editor_url)
        val url = "http://192.168.1.153:8043/view/NoteEditor"
        editor.url = url
        noteId = intent.getIntExtra("noteId", 0)
        if (noteId != 0) {
            editor.setNoteId(noteId)
        }
        val localTimeStamp = SharedPreferencesUtil.getLong(SVTSConstants.localTimeStamp, 0)
        val serverTimeStamp = SharedPreferencesUtil.getLong(SVTSConstants.serverTimeStamp, 0)
        val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
        val timeStamp = TimeStampMD5andKL.JiamiByMiYue(localTimeStamp, serverTimeStamp)
        editor.setTimeStampAndToken(timeStamp, token)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        //添加方法给js调用
        editor.addJavascriptInterface(this,"native")
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        if (!Constants.IS_NIGHT) {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .keyboardEnable(true)
                    .fitsSystemWindows(true)
        } else {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .keyboardEnable(true)
                    .fitsSystemWindows(true)
        }
        mImmersionBar.init()
    }

    @JavascriptInterface
    fun getContentImage(){
        mAction = 1
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
                            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                            startActivityForResult(intent, 1)
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

    @JavascriptInterface
    fun saveSuccess(){
        toastShow("保存成功")
        finish()
    }

    @JavascriptInterface
    fun saveFail(){
        toastError("保存失败")
    }

    @JavascriptInterface
    fun lack(string: String){
        toastError(string)
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType, uri: Uri, imagePath: String) {
        var path: String? = FileUtil.getRealFilePathFromUri(mActivity, uri)
        if (path == null) {
            path = imagePath
        }
        val intent = Intent(mActivity, ClipImageActivity::class.java)
        intent.putExtra("path", path)
        intent.putExtra("aspectX", 16)
        intent.putExtra("aspectY", 9)
        startActivityForResult(intent,Constants.CLIP_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.CLIP_IMAGE) {
                //图片裁剪完成
                val path = data.getStringExtra("path")
                uploadImageToOSS(path)
            }else {
                //图片选择完成
                var photoUri = data.data
                var imagePath: String = ""
                if (photoUri != null) {
                    imagePath = FileUtil.getRealFilePathFromUri(mActivity, photoUri)
                } else {
                    val extras = data.extras
                    if (extras != null) {
                        val imageBitmap = extras.get("data") as Bitmap
                        photoUri = Uri
                                .parse(MediaStore.Images.Media.insertImage(
                                        mActivity.contentResolver, imageBitmap, null, null))

                        imagePath = FileUtil.getRealFilePathFromUri(mActivity, photoUri)
                    }
                }
                uploadImageToOSS(imagePath)
            }
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
        when(mAction) {
            0 -> {
                editor.setTitleImage(url)
            }
            1 -> {
                editor.setContentImage(url)
            }
        }
    }

    override fun uploadFail(message: String?) {
        toastError(message)
    }

    override fun onActionModeStarted(mode: ActionMode) {
        if (mActionMode == null) {
            super.onActionModeStarted(mode)
            mActionMode = mode
            val menu = mode.menu
            menu.clear()
            val menuItemClickListener = MenuItemClickListener()
            menu.add(0, 0, 0, "保存笔记").setOnMenuItemClickListener(menuItemClickListener)
        }
    }

    override fun onActionModeFinished(mode: ActionMode) {
        mActionMode = null
        editor.clearFocus()//移除高亮显示,如果不移除在三星s6手机上会崩溃
        super.onActionModeFinished(mode)
    }

    private inner class MenuItemClickListener : MenuItem.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem): Boolean {
            editor.setBold()
            mActionMode?.finish()
            return true
        }
    }
}
