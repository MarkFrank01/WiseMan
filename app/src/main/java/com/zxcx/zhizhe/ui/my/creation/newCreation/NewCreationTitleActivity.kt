package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SaveFreedomNoteSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_new_creation1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创作-标题题图编辑页
 */
class NewCreationTitleActivity : BaseActivity() , OSSDialog.OSSUploadListener{

    private lateinit var mOSSDialog: OSSDialog
    private var cardBagId: Int? = null
    private var cardId: Int? = null
    private var title: String? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_creation1)
        EventBus.getDefault().register(this)
        initToolBar("创作")
        initData()
        Utils.setViewAspect(fl_new_creation_1_add_img,16,9)
        initViewListener()
        updateView()
        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
    }

    private fun initData() {
        cardId = intent.getIntExtra("cardId",0)
        cardBagId = intent.getIntExtra("cardBagId",0)
        title = intent.getStringExtra("title")
        imageUrl = intent.getStringExtra("imageUrl")

        et_new_creation_1_title.setText(title)
        ImageLoader.load(mActivity,imageUrl,R.color.line,iv_new_creation_1_add_img)
        updateView()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SaveFreedomNoteSuccessEvent) {
        finish()
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(fragmentManager, "")
    }

    override fun uploadSuccess(url: String) {
        imageUrl?.let { deleteImageFromOSS(it)}
        imageUrl = url
        ImageLoader.load(mActivity,imageUrl,iv_new_creation_1_add_img)
        updateView()
    }

    private fun deleteImageFromOSS(oldImageUrl: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 2)
        bundle.putString("url", oldImageUrl)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(fragmentManager, "")
    }

    private fun updateView() {
        tv_new_creation_1_next.isEnabled = et_new_creation_1_title.length() >0 && !StringUtils.isEmpty(imageUrl)
        fl_new_creation_1_add_img.isClickable = et_new_creation_1_title.length() >0
        if (StringUtils.isEmpty(imageUrl)){
            ll_new_creation_1_add_img.visibility = View.VISIBLE
        }else{
            ll_new_creation_1_add_img.visibility = View.GONE
        }
    }

    private fun initViewListener() {
        fl_new_creation_1_add_img.setOnClickListener {
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
        et_new_creation_1_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateView()
            }

        })
        tv_new_creation_1_next.setOnClickListener {
            //进入卡包选择页
            val intent = Intent(mActivity, SelectCardBagActivity::class.java)
            intent.putExtra("cardId", cardId)
            intent.putExtra("cardBagId", cardBagId)
            intent.putExtra("title", et_new_creation_1_title.text.toString())
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == Constants.CLIP_IMAGE){
                //图片裁剪完成
                uploadImageToOSS(data.getStringExtra("path"))
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
                val intent = Intent(mActivity, ClipImageActivity::class.java)
                intent.putExtra("path", imagePath)
                intent.putExtra("aspectX", 16)
                intent.putExtra("aspectY", 9)
                startActivityForResult(intent,Constants.CLIP_IMAGE)
            }
        }
    }
}
