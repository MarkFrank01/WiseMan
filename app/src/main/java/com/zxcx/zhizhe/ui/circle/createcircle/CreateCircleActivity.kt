package com.zxcx.zhizhe.ui.circle.createcircle

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.gyf.barlibrary.ImmersionBar
import com.lxj.xpopup.XPopup
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_create_circle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
//class CreateCircleActivity : BaseActivity(),
//        OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {
class CreateCircleActivity :MvpActivity<CreateCirclePresenter>(),CreateCircleContract.View,
        OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {

    private lateinit var mOSSDialog: OSSDialog
    private lateinit var articleList:List<Int>

    //标题
    private var title =""

    //封面图
    private var mImageUrl = ""

    //类别名
    private var labelName = ""

    //类别ID
    private var classifyId = 0

    //圈子签名
    private var sign = ""

    //圈子等级
    private var mLevel = 0

    //显示的圈子的等级
    private var mLevelName = ""

    //管理文章回传得到的数据
    private var mBackList:List<Int> = ArrayList()


    private lateinit var arrList :MutableList<Map<String,String>>
    private lateinit var arrListItem:Map<String,String>

    companion object {
        const val CODE_SELECT_LABEL = 110
        const val CODE_SELECT_MANAGE = 111
    }

    override fun createPresenter(): CreateCirclePresenter {
        return CreateCirclePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_circle)
        articleList = ArrayList()

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    override fun setListener() {

        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

       mycircle_pic.setOnClickListener {
           getContentImage()
       }

        ll_circle_choose_circle.setOnClickListener {
            val intent = Intent(this, SelectCircleLabelActivity::class.java)
            startActivityForResult(intent, CreateCircleActivity.CODE_SELECT_LABEL)
        }

        create_push_check.setOnClickListener {
            val intent1 = Intent(this,ManageCreateCircleActivity::class.java)
            startActivityForResult(intent1,CreateCircleActivity.CODE_SELECT_MANAGE)
        }

        create_manage_content.setOnClickListener {
//            startActivity(Intent(this,ManageCreateCircleActivity::class.java))
        }

        create_push_level.setOnClickListener {
            chooseLevel()
        }

        tv_toolbar_right.setOnClickListener {
            //标题 封面图 类别id 圈子签名 圈子价格  推荐的文章articleList

            //标题
            title = create_title.text.toString().trim()

            //封面图
//            mImageUrl

            //类别ID
//            classifyId

            //签名
            sign = create_sign.text.toString().trim()

            //圈子价格 0

            //推荐文章
//            arrList.add(arrListItem)

            if (title!=""&&mImageUrl!=""&&classifyId!=0&&sign!=""&&mLevel!=0) {
                mPresenter.createCircle(title, mImageUrl, classifyId, sign, "", articleList,mLevel)
            }else{
                toastShow("信息未填写完")
            }
        }
    }

    override fun uploadSuccess(url: String) {
        LogCat.e("MyPic url is $url")
        ImageLoader.load(mActivity,url,R.drawable.default_card,mycircle_pic_pic)
        mImageUrl = url
    }

    override fun uploadFail(message: String?) {
        toastError(message)
    }

    override fun onGetSuccess(uriType: GetPicBottomDialog.UriType?, uri: Uri?, imagePath: String?) {
        var path: String? = FileUtil.getRealFilePathFromUri(mActivity, uri)
        if (path == null) {
            path = imagePath
        }

        val intent = Intent(mActivity, ClipImageActivity::class.java)
        intent.putExtra("path", path)
        intent.putExtra("aspectX", 4)
        intent.putExtra("aspectY", 3)
        startActivityForResult(intent, Constants.CLIP_IMAGE)
    }

    private fun getContentImage() {
        runOnUiThread {
            val rxPermissions = RxPermissions(this)
            rxPermissions
                    .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { permission ->
                        when {
                            permission.granted -> {
                                // `permission.name` is granted !
                                val getPicBottomDialog = GetPicBottomDialog()
                                val bundle = Bundle()
                                bundle.putString("title", "添加封面图")
                                getPicBottomDialog.arguments = bundle
                                getPicBottomDialog.setListener(this)
                                getPicBottomDialog.setNoCrop(true)
                                getPicBottomDialog.show(supportFragmentManager, "UserInfo")
                            }
                            permission.shouldShowRequestPermissionRationale -> // Denied permission without ask never again
                                toastShow("权限已被拒绝！无法进行操作")
                            else -> {
                                // Denied permission with ask never again
                                // Need to go to the settings
                                val permissionDialog = PermissionDialog()
                                permissionDialog.show(supportFragmentManager, "")
                            }
                        }
                    }
        }
    }

    fun chooseLevel() {
        XPopup.get(mActivity).asBottomList("", arrayOf("黄金","白银","青铜","黑铁"),
                null,-1
        ) { position, text -> {}.run {
            mLevel = position
            circle_tv_level_name.text = text
        } }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                1 -> {
                    //图片选择完成
                    var photoUri = data.data
                    var imagePath = ""
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
                Constants.CLIP_IMAGE -> {
                    //图片裁剪完成
                    val path = data.getStringExtra("path")
                    uploadImageToOSS(path)
                }
                CreateCircleActivity.CODE_SELECT_LABEL -> {
                    labelName = data.getStringExtra("classifyName")
                    classifyId = data.getIntExtra("classifyId",0)
                    circle_tv_label.text = labelName
                }
                CreateCircleActivity.CODE_SELECT_MANAGE ->{
                    mBackList = data.getIntegerArrayListExtra("manageCreateList")
                    LogCat.e("BACK MANAGE"+mBackList.size)
                }
            }
        }
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
    }


    override fun postSuccess(bean: CircleBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: CircleBean?) {
        toastShow("提交成功")
        finish()
    }

}