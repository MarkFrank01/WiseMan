package com.zxcx.zhizhe.ui.my.userInfo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.BitmapUtil
import com.zxcx.zhizhe.utils.FileUtil
import com.zxcx.zhizhe.widget.CropImageView
import kotlinx.android.synthetic.main.activity_image_crop.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * 头像裁剪页面
 */

class ImageCropActivity : BaseActivity(), CropImageView.OnBitmapSaveCompleteListener {

	private var mBitmap: Bitmap? = null
	private var mIsSaveRectangle: Boolean = true
	private var mOutputX: Int = 0
	private var mOutputY: Int = 0
	private var bitmapDegree: Int = 0 //默认旋转角度

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_image_crop)

		//初始化View
		cv_crop_image.setOnBitmapSaveCompleteListener(this)

		//获取需要的参数
		mOutputX = 512
		mOutputY = 512
		val imagePath = intent.getStringExtra("path")

		cv_crop_image.focusStyle = CropImageView.Style.CIRCLE

		//缩放图片
		val options = BitmapFactory.Options()
		options.inJustDecodeBounds = true
		BitmapFactory.decodeFile(imagePath, options)
		val displayMetrics = resources.displayMetrics
		options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels)
		options.inJustDecodeBounds = false
		mBitmap = BitmapFactory.decodeFile(imagePath, options)
		//        cv_crop_image.setImageBitmap(mBitmap);
		//设置默认旋转角度
		bitmapDegree = BitmapUtil.getBitmapDegree(imagePath)
		cv_crop_image.setImageBitmap(cv_crop_image.rotate(mBitmap, bitmapDegree))

		//        cv_crop_image.setImageURI(Uri.fromFile(new File(imagePath)));
	}

	override fun initStatusBar() {

	}

	private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
		val width = options.outWidth
		val height = options.outHeight
		var inSampleSize = 1
		if (height > reqHeight || width > reqWidth) {
			inSampleSize = if (width > height) {
				width / reqWidth
			} else {
				height / reqHeight
			}
		}
		return inSampleSize
	}

	override fun setListener() {
		tv_crop_cancel.setOnClickListener {
			onBackPressed()
		}
		tv_crop_confirm.setOnClickListener {
			cv_crop_image.saveBitmapToFile(File(FileUtil.PATH_BASE), mOutputX, mOutputY, mIsSaveRectangle)
		}
		iv_crop_rotate.setOnClickListener {
			bitmapDegree += 90
			cv_crop_image.setImageBitmap(cv_crop_image.rotate(mBitmap, bitmapDegree))
		}
	}

	override fun onBitmapSaveSuccess(file: File) {
		Luban.with(this)
				.load(file)                     //传入要压缩的图片
				.setCompressListener(object : OnCompressListener { //设置回调
					override fun onStart() {
						// 压缩开始前调用，可以在方法内启动 loading UI
					}

					override fun onSuccess(file: File) {
						//  压缩成功后调用，返回压缩后的图片文件
						val intent = Intent()
						intent.putExtra("path", file.path)
						setResult(Activity.RESULT_OK, intent)
						finish()
					}

					override fun onError(e: Throwable) {
						//  当压缩过程出现问题时调用
					}
				}).launch()    //启动压缩
	}

	override fun onBitmapSaveError(file: File) {
		toastError("保存出错")
		onBackPressed()
	}

	override fun onDestroy() {
		super.onDestroy()
		cv_crop_image.setOnBitmapSaveCompleteListener(null)
		if (mBitmap != null && !mBitmap!!.isRecycled) {
			mBitmap?.recycle()
			mBitmap = null
		}
	}
}
