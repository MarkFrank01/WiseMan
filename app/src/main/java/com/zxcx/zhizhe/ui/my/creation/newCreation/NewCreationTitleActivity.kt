package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ImageCropSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.widget.OSSDialog
import kotlinx.android.synthetic.main.activity_new_creation1.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创作-标题题图编辑页
 */
class NewCreationTitleActivity : BaseActivity() , OSSDialog.OSSUploadListener{

    private val codeAlbum = 1

    private var imageUrl : String? = null
    private lateinit var mOSSDialog: OSSDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_creation1)
        initToolBar("创作")
        Utils.setViewAspect(fl_new_creation_1_add_img,16,9)
        initViewListener()
        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: ImageCropSuccessEvent) {
        uploadImageToOSS(event.path)
        updateView()
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
    }

    private fun initViewListener() {
        fl_new_creation_1_add_img.setOnClickListener {
            // 激活系统图库，选择一张图片
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
            startActivityForResult(intent, codeAlbum)
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
            intent.putExtra("title", et_new_creation_1_title.text.toString())
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // 这个方法是根据Uri获取Bitmap图片的静态方法

            // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            var photoUri = data.data
            var imagePath :String = ""
            // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (photoUri != null) {
                imagePath = FileUtil.getRealFilePathFromUri(mActivity,photoUri)
            } else {
                // android拍照获得图片URI为空的处理方法http://www.xuebuyuan.com/1929552.html
                // 这样做取得是缩略图,以下链接是取得原始图片
                // http://blog.csdn.net/beyond0525/article/details/8940840
                val extras = data.extras
                if (extras != null) {
                    // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                    // Bitmap imageBitmap =
                    // extras.getParcelable("data");
                    val imageBitmap = extras.get("data") as Bitmap
                    photoUri = Uri
                            .parse(MediaStore.Images.Media.insertImage(
                                    mActivity.contentResolver, imageBitmap, null, null))

                    imagePath = FileUtil.getRealFilePathFromUri(mActivity,photoUri)
                }
            }
            val intent = Intent(mActivity, ClipImageActivity::class.java)
            intent.putExtra("path", imagePath)
            intent.putExtra("aspectX", 16)
            intent.putExtra("aspectY", 9)
            startActivity(intent)
        }
    }
}
