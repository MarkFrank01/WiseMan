package com.zxcx.zhizhe.ui.my.userInfo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.GetPicBottomDialog
import com.zxcx.zhizhe.widget.OSSDialog
import com.zxcx.zhizhe.widget.PermissionDialog
import kotlinx.android.synthetic.main.activity_head_image.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by anm on 2017/12/13.
 */

class ChangeHeadImageActivity : BaseActivity(), GetPicBottomDialog.GetPicDialogListener, IPostPresenter<UserInfoBean>, OSSDialog.OSSUploadListener, OSSDialog.OSSDeleteListener {

	private lateinit var mOSSDialog: OSSDialog
	private var isChanged = false
	private var path: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_head_image)
		initToolBar("修改头像")

		iv_toolbar_right.visibility = View.VISIBLE
		iv_toolbar_right.setImageResource(R.drawable.tv_home_rank)

		val imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
		ImageLoader.load(this, imageUrl, R.drawable.iv_my_head_placeholder, iv_head_image)

		mOSSDialog = OSSDialog()
		mOSSDialog.setUploadListener(this)
		mOSSDialog.setDeleteListener(this)
	}

	override fun onBackPressed() {
		if (isChanged) {
			toastShow(R.string.user_info_change)
		}
		super.onBackPressed()
	}

	override fun setListener() {
		super.setListener()
		iv_toolbar_right.setOnClickListener {
			val rxPermissions = RxPermissions(this)
			rxPermissions
					.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
					.subscribe { permission ->
						when {
							permission.granted -> {
								// `permission.name` is granted !
								val getPicBottomDialog = GetPicBottomDialog()
								val bundle = Bundle()
								bundle.putString("title", "更换头像")
								getPicBottomDialog.arguments = bundle
								getPicBottomDialog.setListener(this@ChangeHeadImageActivity)
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
	}

	override fun onGetSuccess(uriType: GetPicBottomDialog.UriType, uri: Uri, imagePath: String) {
		var path: String? = FileUtil.getRealFilePathFromUri(mActivity, uri)
		if (path == null) {
			path = imagePath
		}
		val intent = Intent(mActivity, ImageCropActivity::class.java)
		intent.putExtra("path", path)
		intent.putExtra("aspectX", 1)
		intent.putExtra("aspectY", 1)
		startActivityForResult(intent, Constants.CLIP_IMAGE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == Constants.CLIP_IMAGE) {
				//图片裁剪完成
				path = data.getStringExtra("path")
				ImageLoader.load(mActivity, path, iv_head_image)
				path?.let { uploadImageToOSS(it) }
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
		changeImageUrl(url)
	}

	override fun uploadFail(message: String) {
		toastError(message)
	}

	fun changeImageUrl(imageUrl: String) {
		mDisposable = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null, null)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main_loading(this))
				.subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
					override fun onNext(bean: UserInfoBean) {
						postSuccess(bean)
					}
				})
	}

	override fun postSuccess(bean: UserInfoBean) {
		val imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
		ZhiZheUtils.saveUserInfo(bean)
		ImageLoader.load(this, bean.avatar, R.drawable.iv_my_head_placeholder, iv_head_image)
		deleteImageFromOSS(imageUrl)
	}

	override fun postFail(msg: String) {
		toastFail(msg)
	}

	private fun deleteImageFromOSS(oldImageUrl: String) {
		val bundle = Bundle()
		bundle.putInt("OSSAction", 2)
		bundle.putString("url", oldImageUrl)
		mOSSDialog.arguments = bundle
		mOSSDialog.show(fragmentManager, "")
	}

	override fun deleteSuccess() {
		isChanged = true
		onBackPressed()
	}

	override fun deleteFail() {
		isChanged = true
		onBackPressed()
	}
}
