package com.zxcx.zhizhe.ui.circle.createcircle

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import com.gyf.barlibrary.ImmersionBar
import com.lxj.xpopup.XPopup
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushCreateCircleListEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_create_circle.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
//class CreateCircleActivity : BaseActivity(),
//        OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {
class CreateCircleActivity : MvpActivity<CreateCirclePresenter>(), CreateCircleContract.View,
        OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {


    private lateinit var mOSSDialog: OSSDialog
    private lateinit var articleList: List<Int>

    val textWatcher1: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            tv_toolbar_right.isEnabled = true

            tv_check1.text = "${s.toString().length}/8"
            tv_check1.setTextColor(mActivity.getColorForKotlin(R.color.text_color_3))
            LogCat.e("${s.toString()}")
            mPresenter.checkName(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    val textWatcher2: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            tv_check2.text = "${s.toString().length}/30"
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    //标题
    private var title = ""

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
    private var mBackList: List<Int> = ArrayList()

    private lateinit var mDialog: PushCreateCircleConfirmDialog

    private lateinit var arrList: MutableList<Map<String, String>>
    private lateinit var arrListItem: Map<String, String>

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
        EventBus.getDefault().register(this)

        articleList = ArrayList()

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)

        mDialog = PushCreateCircleConfirmDialog()
        initView()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    override fun checkSuccess() {
        tv_toolbar_right.isEnabled = false
        tv_check1.text = "圈名重复了"
        //.setTextColor(R.id.tv_statue, mContext.getColorForKotlin(R.color.button_blue))
        tv_check1.setTextColor(mActivity.getColorForKotlin(R.color.red))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PushCreateCircleListEvent) {
        mPresenter.createCircle(title, mImageUrl, classifyId, sign, "", articleList, mLevel)
    }

    override fun setListener() {

        create_xieyi.setOnClickListener {

            startActivity(WebViewActivity::class.java) {
                it.putExtra("title", "智者圈子用户常见问题")
                if (Constants.IS_NIGHT) {
                    it.putExtra("url", getString(R.string.base_url) + getString(R.string.create_circle))
                } else {
                    it.putExtra("url", getString(R.string.base_url) + getString(R.string.create_circle))
                }
            }
        }

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
            val intent1 = Intent(this, ManageCreateCircleActivity::class.java)
            startActivityForResult(intent1, CreateCircleActivity.CODE_SELECT_MANAGE)
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

            if (title != "" && mImageUrl != "" && classifyId != 0 && sign != "" && mLevel != 0) {
                //mPresenter.createCircle(title, mImageUrl, classifyId, sign, "", articleList,mLevel)
                val bundle = Bundle()
                mDialog.arguments = bundle
                mDialog.show(mActivity.supportFragmentManager, "")

            } else {
                toastShow("信息未填写完")
            }
        }
    }

    override fun uploadSuccess(url: String) {
        LogCat.e("MyPic url is $url")
        ImageLoader.load(mActivity, url, R.drawable.default_card, mycircle_pic_pic)
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
//        XPopup.get(mActivity).asBottomList("", arrayOf("黄金", "白银", "青铜", "黑铁"),
//                null, -1
//        ) { position, text ->
//            {}.run {
//                mLevel = position
//                circle_tv_level_name.text = text
//            }
//        }.show()
        XPopup.get(this)
//                .asCustom(CustomPopup(this))
                .asBottomList("", arrayOf("黄金", "白银", "青铜", "黑铁"),
                null, -1
        ){
                    position, text ->
                    {}.run {
                        mLevel = position
                        circle_tv_level_name.text = text
                    }
                }
                .show()
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
                    classifyId = data.getIntExtra("classifyId", 0)
                    circle_tv_label.text = labelName
                }
                CreateCircleActivity.CODE_SELECT_MANAGE -> {
                    mBackList = data.getIntegerArrayListExtra("manageCreateList")
                    LogCat.e("BACK MANAGE" + mBackList.size)
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

    private fun initView() {
        create_title.addTextChangedListener(textWatcher1)
        create_sign.addTextChangedListener(textWatcher2)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            create_xieyi?.text = Html.fromHtml("更多详情，点击<font color='#0088AA'>了解圈子</font>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            create_xieyi?.text = Html.fromHtml("更多详情，点击<font color='#0088AA'>了解圈子</font>")
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tv_reject_agreement?.text = Html.fromHtml("详情请阅读<font color='#0088AA'>智者创作协议</font>", Html.FROM_HTML_MODE_LEGACY)
//        } else {
//            tv_reject_agreement?.text = Html.fromHtml("详情请阅读<font color='#0088AA'>智者创作协议</font>")
//        }
    }
}