package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.*
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
	private var isCard = true

	companion object {
		const val CODE_SELECT_LABEL = 110
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creation_editor)

		initEditor()
		iv_creation_editor_more.visibility = View.VISIBLE

		mOSSDialog = OSSDialog()
		mOSSDialog.setUploadListener(this)
	}

	override fun onBackPressed() {
		if (editor.canGoBack()) {
			editor.goBack()
		} else {
			editor.confirmSave {
				if (it == "true") {
					val dialog = NeedSaveDialog()
					dialog.mCancelListener = {
						super.onBackPressed()
					}
					dialog.mConfirmListener = {
						editor.saveDraft()
					}
					dialog.show(supportFragmentManager, "")
				} else {
					super.onBackPressed()
				}
			}
		}
	}

	private fun initEditor() {
		val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.creation_editor_url)
//		val url = "http://192.168.1.153:8043/pages/card-editor.html"
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
		tv_toolbar_back.setOnClickListener {
			onBackPressed()
		}

		tv_toolbar_right.setOnClickListener {
			editor.saveNote()
		}

		iv_creation_editor_add_image.setOnClickListener {
			getContentImage()
		}

		iv_creation_editor_bold.setOnClickListener {
			editor.setBold()
		}

		iv_creation_editor_center.setOnClickListener {
			editor.setCenter()
		}

		iv_creation_editor_revocation.setOnClickListener {
			editor.rollback()
		}

		tv_toolbar_right.setOnClickListener {
			editor.submitDraft()
		}

		iv_creation_editor_more.setOnClickListener {
			Utils.closeInputMethod(mActivity)
			val window = CreationMoreWindow(mActivity, isCard)
			window.mPreviewListener = {
				editor.editPreview()
			}
			window.mSaveListener = {
				editor.saveDraft()
			}
			window.mTypeListener = {
				if (!isCard) {
					val dialog = ChangeToCardDialog()
					dialog.mListener = {
						editor.checkEditType(0)
						isCard = !isCard
						iv_creation_editor_add_image.visibility = View.GONE
					}
					dialog.show(supportFragmentManager, "")
				} else {
					editor.checkEditType(1)
					isCard = !isCard
					iv_creation_editor_add_image.visibility = View.VISIBLE
				}
			}
			window.mDeleteListener = {
				val dialog = DeleteCreationDialog()
				dialog.mListener = {
					//					editor.deleteEdit()
					finish()
				}
				dialog.show(supportFragmentManager, "")
			}
			window.showAtLocation(iv_creation_editor_more, Gravity.BOTTOM or Gravity.RIGHT,
					0, ScreenUtils.dip2px(24f))
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
							permissionDialog.show(supportFragmentManager, "")
						}
					}
				}
	}

	@JavascriptInterface
	fun saveSuccess() {
		toastShow("保存草稿成功")
		EventBus.getDefault().post(SaveDraftSuccessEvent())
		startActivity(CreationActivity::class.java) {
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			it.putExtra("goto", 3)
		}
		finish()
	}

	@JavascriptInterface
	fun saveFail() {
		toastError("保存草稿失败")
	}

	@JavascriptInterface
	fun commitSuccess() {
		toastShow("提交审核成功")
		EventBus.getDefault().post(CommitCardReviewEvent())
		startActivity(CreationActivity::class.java) {
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			it.putExtra("goto", 1)
		}
		finish()
	}

	@JavascriptInterface
	fun commitFail() {
		toastError("提交审核失败")
	}

	@JavascriptInterface
	fun getLabel() {
		val intent = Intent(this, SelectLabelActivity::class.java)
		startActivityForResult(intent, CODE_SELECT_LABEL)
	}

	@JavascriptInterface
	fun hiddenToolBar() {
		runOnUiThread {
			iv_creation_editor_add_image.isClickable = false
			iv_creation_editor_bold.isClickable = false
			iv_creation_editor_center.isClickable = false
			iv_creation_editor_revocation.isClickable = false
			ImageLoader.load(mActivity, R.drawable.iv_add_image_no_clickable, iv_creation_editor_add_image)
			ImageLoader.load(mActivity, R.drawable.iv_bold_no_clickable, iv_creation_editor_bold)
			ImageLoader.load(mActivity, R.drawable.iv_center_no_clickable, iv_creation_editor_center)
			ImageLoader.load(mActivity, R.drawable.iv_revocation_no_clickable, iv_creation_editor_revocation)
		}
	}

	@JavascriptInterface
	fun showToolBar() {
		runOnUiThread {
			iv_creation_editor_add_image.isClickable = true
			iv_creation_editor_bold.isClickable = true
			iv_creation_editor_center.isClickable = true
			iv_creation_editor_revocation.isClickable = true
			ImageLoader.load(mActivity, R.drawable.iv_add_image_clickable, iv_creation_editor_add_image)
			ImageLoader.load(mActivity, R.drawable.iv_bold_no_checked, iv_creation_editor_bold)
			ImageLoader.load(mActivity, R.drawable.iv_center_no_checked, iv_creation_editor_center)
			ImageLoader.load(mActivity, R.drawable.iv_revocation_clickable, iv_creation_editor_revocation)
		}
	}

	@JavascriptInterface
	fun judgeJustify(isCenter: Boolean) {
		runOnUiThread {
			ImageLoader.load(mActivity, if (isCenter) R.drawable.iv_center_checked else R.drawable.iv_center_no_checked,
					iv_creation_editor_center)
		}
	}

	@JavascriptInterface
	fun judgeBold(isBold: Boolean) {
		runOnUiThread {
			ImageLoader.load(mActivity, if (isBold) R.drawable.iv_bold_checked else R.drawable.iv_bold_no_checked,
					iv_creation_editor_bold)
		}
	}

	@JavascriptInterface
	fun judgeSubmit(isEnable: Boolean) {
		runOnUiThread {
			tv_toolbar_right.isEnabled = isEnable
		}
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
		intent.putExtra("aspectX", if (isCard) 4 else 16)
		intent.putExtra("aspectY", if (isCard) 3 else 9)
		startActivityForResult(intent, Constants.CLIP_IMAGE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == 1) {
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
			} else if (requestCode == CODE_SELECT_LABEL) {
				val labelName = data.getStringExtra("labelName")
				val classifyId = data.getIntExtra("classifyId", 0)
				editor.setLabel(labelName, classifyId)
			} else if (requestCode == Constants.CLIP_IMAGE) {
				//图片裁剪完成
				val path = data.getStringExtra("path")
				uploadImageToOSS(path)
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
