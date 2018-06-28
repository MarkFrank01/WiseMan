package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_creation_editor.*
import org.greenrobot.eventbus.EventBus

class CreationEditorActivity : BaseActivity(),
		OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {

	private lateinit var mOSSDialog: OSSDialog

	private var mAction = 0 //0选择标题图，1选择内容图
	private var cardId = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creation_editor)

		initEditor()

		mOSSDialog = OSSDialog()
		mOSSDialog.setUploadListener(this)
	}

	private fun initEditor() {
		val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.creation_editor_url)
//        val url = "http://192.168.1.153:8043/view/mobile-editor"
		editor.url = url
		cardId = intent.getIntExtra("cardId", 0)
		val type = intent.getIntExtra("type", 0)
		val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
		if (cardId != 0) {
			editor.articleReedit(cardId, token, type)
		} else {
			editor.setTimeStampAndToken(token)
		}
	}

	override fun setListener() {
		iv_common_close.setOnClickListener {
			onBackPressed()
		}

		//添加方法给js调用
		editor.addJavascriptInterface(this, "native")
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
	fun getTitleImage() {
		mAction = 0
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
							getPicBottomDialog.show(fragmentManager, "UserInfo")
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
	fun getContentImage() {
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
	fun saveSuccess() {
		toastShow("保存草稿成功")
		EventBus.getDefault().post(SaveDraftSuccessEvent())
		finish()
	}

	@JavascriptInterface
	fun commitSuccess() {
		toastShow("提交审核成功")
		EventBus.getDefault().post(CommitCardReviewEvent())
		finish()
	}

	@JavascriptInterface
	fun saveFail() {
		toastError("保存草稿失败")
	}

	@JavascriptInterface
	fun commitFail() {
		toastError("提交审核失败")
	}

	@JavascriptInterface
	fun lack(string: String) {
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
		startActivityForResult(intent, Constants.CLIP_IMAGE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == Constants.CLIP_IMAGE) {
				//图片裁剪完成
				val path = data.getStringExtra("path")
				uploadImageToOSS(path)
			} else {
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
		when (mAction) {
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
}
