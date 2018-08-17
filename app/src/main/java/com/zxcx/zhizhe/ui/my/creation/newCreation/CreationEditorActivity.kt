package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import com.gyf.barlibrary.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.DeleteCreationEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.userInfo.ClipImageActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import com.zxcx.zhizhe.widget.UploadingDialog
import kotlinx.android.synthetic.main.activity_creation_editor.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创作-编辑器页面
 */

class CreationEditorActivity : BaseActivity(),
		OSSDialog.OSSUploadListener, GetPicBottomDialog.GetPicDialogListener {

	private lateinit var mOSSDialog: OSSDialog
	private lateinit var mUploadingDialog: UploadingDialog

	private var mAction = 0 //0选择标题图，1选择内容图
	private var cardId = 0
	private var isCard = true
	private var isBold = false
	private var isCenter = false
	private var labelName = ""
	private var classifyId = 0

	companion object {
		const val CODE_SELECT_LABEL = 110
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_creation_editor)

		initEditor()
		iv_creation_editor_add_image.visibility = View.GONE
		iv_creation_editor_more.visibility = View.VISIBLE

		mOSSDialog = OSSDialog()
		mOSSDialog.setUploadListener(this)
		mUploadingDialog = UploadingDialog()
	}

	override fun onBackPressed() {
		editor.exitEdit()
	}

	override fun onDestroy() {
		if (editor != null) {
			editor.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
			editor.clearHistory()

			(editor.parent as ViewGroup).removeView(editor)
			editor.destroy()
		}
		super.onDestroy()
	}

	private fun initEditor() {
		val url = mActivity.getString(R.string.base_url) + mActivity.getString(R.string.creation_editor_url)
//		val url = "http://192.168.1.153:8043/pages/card-editor.html"
		editor.url = url
		cardId = intent.getIntExtra("cardId", 0)
        isCard = intent.getBooleanExtra("isCard",true)
		val token = SharedPreferencesUtil.getString(SVTSConstants.token, "")
		if (cardId != 0) {
			editor.articleReedit(cardId, token)
		} else {
			editor.setTimeStampAndToken(token)
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: CommitCardReviewEvent) {
		Utils.closeInputMethod(mActivity)
		finish()
	}

	override fun setListener() {
		tv_toolbar_back.setOnClickListener {
			onBackPressed()
		}

		tv_toolbar_right.setOnClickListener {
			val bundle = Bundle()
			bundle.putString("uploadingText", "正在提交")
			bundle.putString("successText", "审核中")
			bundle.putString("failText", "提交失败")
			mUploadingDialog.arguments = bundle
			mUploadingDialog.show(supportFragmentManager, "")
			Handler().postDelayed({
				editor.submitDraft()
			}, 500)
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

		iv_creation_editor_more.setOnClickListener {
			Utils.closeInputMethod(mActivity)
			val window = CreationMoreWindow(mActivity, isCard)
			window.mPreviewListener = {
				editor.editPreview()
			}
			window.mSaveListener = {
				val bundle = Bundle()
				bundle.putString("uploadingText", "正在保存草稿")
				bundle.putString("successText", "保存成功")
				bundle.putString("failText", "保存失败")
				mUploadingDialog.arguments = bundle
				mUploadingDialog.show(supportFragmentManager, "")
				Handler().postDelayed({
					editor.saveDraft()
				}, 500)
			}
			window.mTypeListener = {
				if (!isCard) {
					val dialog = ChangeToCardDialog()
					dialog.mListener = {
						editor.checkEditType(0)
						isCard = !isCard
					}
					dialog.show(supportFragmentManager, "")
				} else {
					editor.checkEditType(1)
					isCard = !isCard
				}
			}
			window.mDeleteListener = {
				val dialog = DeleteCreationDialog()
				dialog.mListener = {
					editor.deleteEdit()
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
		runOnUiThread {
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
	}

	private fun getContentImage() {
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
		runOnUiThread {
			mUploadingDialog.setSuccess(true)
		}
		Handler().postDelayed({
			EventBus.getDefault().post(SaveDraftSuccessEvent())
			startActivity(CreationActivity::class.java) {
				it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				it.putExtra("goto", 3)
			}
			Utils.closeInputMethod(mActivity)
			finish()
		}, 1000)
	}

	@JavascriptInterface
	fun saveFail() {
		runOnUiThread {
			mUploadingDialog.setSuccess(false)
		}
	}

	@JavascriptInterface
	fun submitSuccess() {
		runOnUiThread {
			mUploadingDialog.setSuccess(true)
		}
		Handler().postDelayed({
			EventBus.getDefault().post(CommitCardReviewEvent())
			startActivity(CreationActivity::class.java) {
				it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				it.putExtra("goto", 1)
			}
			Utils.closeInputMethod(mActivity)
			finish()
		}, 1000)
	}

	@JavascriptInterface
	fun submitFail() {
		runOnUiThread {
			mUploadingDialog.setSuccess(false)
		}
	}

	@JavascriptInterface
	fun deleteSuccess() {
		toastShow("删除成功")
		EventBus.getDefault().post(DeleteCreationEvent())
		Utils.closeInputMethod(mActivity)
		finish()
	}

	@JavascriptInterface
	fun deleteFail() {
		toastError("删除失败")
	}

	@JavascriptInterface
	fun preview(previewId: String) {
        if(isCard) {
            startActivity(PreviewCardDetailsActivity::class.java) {
                val cardBean = CardBean()
                cardBean.id = previewId.toInt()
                it.putExtra("cardBean", cardBean)
            }
        }else{
            startActivity(CreationPreviewActivity::class.java){
                it.putExtra("id",previewId)
            }
        }
	}

	@JavascriptInterface
	fun getLabel() {
		val intent = Intent(this, SelectLabelActivity::class.java)
		intent.putExtra("labelName", labelName)
		intent.putExtra("classifyId", classifyId)
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
			ImageLoader.load(mActivity, if (isBold) R.drawable.iv_bold_checked else R.drawable.iv_bold_no_checked,
					iv_creation_editor_bold)
			ImageLoader.load(mActivity, if (isCenter) R.drawable.iv_center_checked else R.drawable.iv_center_no_checked,
					iv_creation_editor_center)
			ImageLoader.load(mActivity, R.drawable.iv_revocation_clickable, iv_creation_editor_revocation)
		}
	}

	@JavascriptInterface
	fun judgeJustify(isCenter: Boolean) {
		this.isCenter = isCenter
		runOnUiThread {
			ImageLoader.load(mActivity, if (isCenter) R.drawable.iv_center_checked else R.drawable.iv_center_no_checked,
					iv_creation_editor_center)
		}
	}

	@JavascriptInterface
	fun judgeBold(isBold: Boolean) {
		this.isBold = isBold
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
	fun judgeIsCard(isCard: Boolean) {
		this.isCard = isCard
		runOnUiThread {
			iv_creation_editor_add_image.visibility = if (isCard) View.GONE else View.VISIBLE
		}
	}

	@JavascriptInterface
	fun confirmSave(isNeedSave: Boolean) {
		if (isNeedSave) {
			val dialog = NeedSaveDialog()
			dialog.mCancelListener = {
				Utils.closeInputMethod(mActivity)
				finish()
			}
			dialog.mConfirmListener = {
				val bundle = Bundle()
				bundle.putString("uploadingText", "正在保存草稿")
				bundle.putString("successText", "保存成功")
				bundle.putString("failText", "保存失败")
				mUploadingDialog.arguments = bundle
				mUploadingDialog.show(supportFragmentManager, "")
				Handler().postDelayed({
					editor.saveDraft()
				}, 500)
			}
			dialog.show(supportFragmentManager, "")
		} else {
			Utils.closeInputMethod(mActivity)
			finish()
		}
	}

	@JavascriptInterface
	fun lackSomething(string: String) {
		runOnUiThread {
			if (mUploadingDialog.isAdded) {
				mUploadingDialog.setSuccess(false)
			}
		}
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
				CODE_SELECT_LABEL -> {
					labelName = data.getStringExtra("labelName")
					classifyId = data.getIntExtra("classifyId", 0)
					editor.setLabel(labelName, classifyId)
				}
				Constants.CLIP_IMAGE -> {
					//图片裁剪完成
					val path = data.getStringExtra("path")
					uploadImageToOSS(path)
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
