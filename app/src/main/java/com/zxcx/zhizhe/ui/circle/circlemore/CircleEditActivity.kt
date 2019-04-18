package com.zxcx.zhizhe.ui.circle.circlemore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushCreateCircleListEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.createcircle.CreateCircleActivity
import com.zxcx.zhizhe.ui.circle.createcircle.CreateCircleDescActivity
import com.zxcx.zhizhe.ui.circle.createcircle.CreateCircleNameActivity
import com.zxcx.zhizhe.ui.circle.createcircle.PushCreateCircleConfirmDialog
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.BottomListPopup.CirclePopup
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import com.zxcx.zhizhe.widget.bottomdescpopup.CircleEditBottomPopup
import com.zxcx.zhizhe.widget.bottominfopopup.BottomInfoPopup
import kotlinx.android.synthetic.main.activity_edit_circle.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class CircleEditActivity : MvpActivity<CircleEditPresenter>(), CircleEditContract.View,
        OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {

    private lateinit var mOSSDialog: OSSDialog

    //圈子的ID
    private var circleId = 0

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

    //（新）价格选择
    private var levelType = 0

    //(新)限时免费
    private var limitedTimeType = 0

    private var mSelectPosition = 0
    private var mSelectPosition2 = 0

    private lateinit var mDialog: PushCreateCircleConfirmDialog


    companion object {
        const val CODE_SELECT_LABEL = 110
        const val CODE_SELECT_MANAGE = 111
        const val CODE_CREATE_NAME = 112
        const val CODE_DESC = 113
    }

    override fun createPresenter(): CircleEditPresenter {
        return CircleEditPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_circle)
        EventBus.getDefault().register(this)
        initData()

        mOSSDialog = OSSDialog()
        mOSSDialog.setUploadListener(this)

        mDialog = PushCreateCircleConfirmDialog()

        initView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PushCreateCircleListEvent) {
//        mPresenter.createCircle(title, mImageUrl, classifyId, sign, "", mBackList, mLevel)
        mPresenter.createCircleNew(title, mImageUrl, classifyId, sign, levelType, limitedTimeType,circleId)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun checkSuccess() {
        toastShow("提交成功，等待审核")
        onBackPressed()
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: CircleBean?) {
//        onBackPressed()
    }



    override fun uploadSuccess(url: String) {
        ImageLoader.load(mActivity, url, R.drawable.default_card, mycircle_pic_pic)
        mImageUrl = url
    }

    override fun uploadFail(message: String?) {
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

                CreateCircleActivity.CODE_CREATE_NAME -> {
                    tv_to_name.text = data.getStringExtra("circle_title")
                }
                CreateCircleActivity.CODE_DESC -> {
                    tv_to_name2.text = data.getStringExtra("circle_desc")
                }
            }
        }
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

        iv_to_name.setOnClickListener {
            val intent = Intent(this, CreateCircleNameActivity::class.java)
            startActivityForResult(intent, CircleEditActivity.CODE_CREATE_NAME)
        }

        iv_to_name2.setOnClickListener {
            val intent = Intent(this, CreateCircleDescActivity::class.java)
            startActivityForResult(intent, CircleEditActivity.CODE_DESC)
        }

        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        mycircle_pic.setOnClickListener {
            getContentImage()
        }

        ll_circle_choose_circle.setOnClickListener {
            //编辑圈子的时候分类不可选
//            val intent = Intent(this, SelectCircleLabelActivity::class.java)
//            startActivityForResult(intent, CircleEditActivity.CODE_SELECT_LABEL)
        }

        create_push_check.setOnClickListener {
            chooseFreeTime()
        }

        create_push_level.setOnClickListener {
            chooseMoney()
        }



        tv_toolbar_right2.setOnClickListener {
            title = tv_to_name.text.toString().trim()
            sign = tv_to_name2.text.toString().trim()

            if (title != "" && mImageUrl != "" && classifyId != 0 && sign != "" && levelType != -1 && limitedTimeType != -1) {
//                val bundle = Bundle()
//                mDialog.arguments = bundle
//                mDialog.show(mActivity.supportFragmentManager, "")
                mPresenter.createCircleNew(title, mImageUrl, classifyId, sign, levelType, limitedTimeType,circleId)
            } else {
                toastShow("信息未填写完")
            }
        }

        tv_toolbar_right3.setOnClickListener {
            title = tv_to_name.text.toString().trim()
            sign = tv_to_name2.text.toString().trim()

            if (title != "" && mImageUrl != "" && classifyId != 0 && sign != "" && levelType != -1 && limitedTimeType != -1) {
                val bundle = Bundle()
                mDialog.arguments = bundle
                mDialog.show(mActivity.supportFragmentManager, "")
            } else {
                toastShow("信息未填写完")
            }
        }
    }

    private fun initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            create_xieyi?.text = Html.fromHtml("未提交的圈子可保留30天有效期，详情点击<font color='#0088AA'>了解圈子</font>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            create_xieyi?.text = Html.fromHtml("未提交的圈子可保留30天有效期，详情点击<font color='#0088AA'>了解圈子</font>")
        }

        ImageLoader.load(mActivity, mImageUrl, R.drawable.default_card, mycircle_pic_pic)

        tv_to_name.text = title
        tv_to_name2.text = sign
        circle_tv_label.text = labelName

        hintTime()
    }

    private fun initData() {
        title = intent.getStringExtra("title")
        levelType = intent.getIntExtra("levelType", 0)
        sign = intent.getStringExtra("sign")
        mImageUrl = intent.getStringExtra("mImageUrl")
        labelName = intent.getStringExtra("labelName")
        classifyId = intent.getIntExtra("classifyId", 0)
        limitedTimeType = intent.getIntExtra("limitedTimeType", 0)

        circleId = intent.getIntExtra("circleId",0)
    }

    private fun uploadImageToOSS(path: String) {
        val bundle = Bundle()
        bundle.putInt("OSSAction", 1)
        bundle.putString("filePath", path)
        mOSSDialog.arguments = bundle
        mOSSDialog.show(supportFragmentManager, "")
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


    //选择价钱
    private fun chooseMoney() {
        XPopup.Builder(mActivity)
                .asCustom(CirclePopup(this, "付费进圈", arrayOf("￥ 98.00", "￥ 40.00", "￥ 18.00"),
                        null, -1,
                        OnSelectListener { position, text ->
                            mSelectPosition = position
                            circle_tv_level_name.text = text
                            when (position) {
                                0 -> levelType = 3
                                1 -> levelType = 2
                                3 -> levelType = 1
                            }
                        })
                ).show()
    }

    //限时免费
    private fun chooseFreeTime() {
        XPopup.Builder(mActivity)
                .asCustom(CirclePopup(this, "限时免费", arrayOf("3个月", "1个月", "无"),
                        null, -1,
                        OnSelectListener { position, text ->
                            mSelectPosition2 = position
                            circle_tv_free_time.text = text
                            when (position) {
                                0 -> limitedTimeType = 3
                                1 -> limitedTimeType = 2
                                2 -> limitedTimeType = 1
                            }
                        })
                ).show()
    }

    //检查退出
    private fun showCancel() {
        XPopup.Builder(mActivity)
                .asCustom(BottomInfoPopup(this, "还有编辑未完成，是否退出？", -1,
                        OnSelectListener { position, text ->
                            if (position == 2) {
                                onBackPressed()
                            }
                        })
                ).show()
    }

    //提示时间
    private fun hintTime(){
        XPopup.Builder(mActivity)
                .asCustom(CircleEditBottomPopup(this, OnSelectListener { position, text ->

                     })
                ).show()
    }
}