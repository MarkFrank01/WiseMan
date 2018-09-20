package com.zxcx.zhizhe.ui.my.userInfo

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.os.Bundle
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.utils.FileUtil
import kotlinx.android.synthetic.main.activity_head_image_crop.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 创作图片裁剪页面
 */

class ClipImageActivity : BaseActivity() {


	private var mInput: String? = null
	private var mMaxWidth: Int = 0

	// 图片被旋转的角度
	private var mDegree: Int = 0
	// 图片主动旋转角度
	private var mRotationDegree: Int = 0

	// 大图被设置之前的缩放比例
	private var mSampleSize: Int = 0
	private var mSourceWidth: Int = 0
	private var mSourceHeight: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_head_image_crop)
		ButterKnife.bind(this)

		mInput = intent.getStringExtra("path")
		mMaxWidth = intent.getIntExtra("maxWidth", 1080)
		cvl_head_image_crop
				.setAspect(intent.getIntExtra("aspectX", 1),
						intent.getIntExtra("aspectY", 1))
		cvl_head_image_crop.setMaxOutputWidth(mMaxWidth)

		setImageAndClipParams() //大图裁剪
	}

	override fun initStatusBar() {

	}

	private fun setImageAndClipParams() {
		cvl_head_image_crop.post {
			cvl_head_image_crop.setMaxOutputWidth(mMaxWidth)

			mDegree = readPictureDegree(mInput)

			mDegree = mDegree + mRotationDegree

			val isRotate = mDegree == 90 || mDegree == 270

			val options = BitmapFactory.Options()
			options.inJustDecodeBounds = true
			BitmapFactory.decodeFile(mInput, options)

			mSourceWidth = options.outWidth
			mSourceHeight = options.outHeight

			// 如果图片被旋转，则宽高度置换
			val w = if (isRotate) options.outHeight else options.outWidth

			// 裁剪是宽高比例3:2，只考虑宽度情况，这里按border宽度的两倍来计算缩放。
			mSampleSize = findBestSample(w, cvl_head_image_crop.clipBorder.width())

			options.inJustDecodeBounds = false
			options.inSampleSize = mSampleSize
			options.inPreferredConfig = Bitmap.Config.RGB_565
			val source = BitmapFactory.decodeFile(mInput, options)

			// 解决图片被旋转的问题
			val target: Bitmap
			if (mDegree == 0) {
				target = source
			} else {
				val matrix = Matrix()
				matrix.postRotate(mDegree.toFloat())
				target = Bitmap
						.createBitmap(source, 0, 0, source.width, source.height, matrix,
								false)
				if (target != source && !source.isRecycled) {
					source.recycle()
				}
			}
			cvl_head_image_crop.setImageBitmap(target)
		}
	}

	private fun clipImage() {
		val bitmap = createClippedBitmap()
		val fileName = FileUtil.getRandomImageName()
		FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName)
		val path = FileUtil.PATH_BASE + fileName
		Luban.with(this)
				.load(File(path))                     //传入要压缩的图片
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

	private fun createClippedBitmap(): Bitmap {
		if (mSampleSize <= 1) {
			return cvl_head_image_crop.clip()
		}

		// 获取缩放位移后的矩阵值
		val matrixValues = cvl_head_image_crop.clipMatrixValues
		val scale = matrixValues[Matrix.MSCALE_X]
		val transX = matrixValues[Matrix.MTRANS_X]
		val transY = matrixValues[Matrix.MTRANS_Y]

		// 获取在显示的图片中裁剪的位置
		val border = cvl_head_image_crop.clipBorder
		val cropX = (-transX + border.left) / scale * mSampleSize
		val cropY = (-transY + border.top) / scale * mSampleSize
		val cropWidth = border.width() / scale * mSampleSize
		val cropHeight = border.height() / scale * mSampleSize

		// 获取在旋转之前的裁剪位置
		val srcRect = RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight)
		val clipRect = getRealRect(srcRect)

		val ops = BitmapFactory.Options()
		val outputMatrix = Matrix()

		outputMatrix.setRotate(mDegree.toFloat())
		// 如果裁剪之后的图片宽高仍然太大,则进行缩小
		if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
			ops.inSampleSize = findBestSample(cropWidth.toInt(), mMaxWidth)

			val outputScale = mMaxWidth / (cropWidth / ops.inSampleSize)
			outputMatrix.postScale(outputScale, outputScale)
		}

		// 裁剪
		var decoder: BitmapRegionDecoder? = null
		return try {
			decoder = BitmapRegionDecoder.newInstance(mInput, false)
			val source = decoder!!.decodeRegion(clipRect, ops)
			recycleImageViewBitmap()
			Bitmap
					.createBitmap(source, 0, 0, source.width, source.height, outputMatrix,
							false)
		} catch (e: Exception) {
			cvl_head_image_crop.clip()
		} finally {
			if (decoder != null && !decoder.isRecycled) {
				decoder.recycle()
			}
		}
	}

	private fun getRealRect(srcRect: RectF): Rect {
		return when (mDegree) {
			90 -> Rect(srcRect.top.toInt(), (mSourceHeight - srcRect.right).toInt(),
					srcRect.bottom.toInt(), (mSourceHeight - srcRect.left).toInt())
			180 -> Rect((mSourceWidth - srcRect.right).toInt(),
					(mSourceHeight - srcRect.bottom).toInt(),
					(mSourceWidth - srcRect.left).toInt(), (mSourceHeight - srcRect.top).toInt())
			270 -> Rect((mSourceWidth - srcRect.bottom).toInt(), srcRect.left.toInt(),
					(mSourceWidth - srcRect.top).toInt(), srcRect.right.toInt())
			else -> Rect(srcRect.left.toInt(), srcRect.top.toInt(), srcRect.right.toInt(),
					srcRect.bottom.toInt())
		}
	}

	private fun recycleImageViewBitmap() {
		cvl_head_image_crop.post { cvl_head_image_crop.setImageBitmap(null) }
	}

	override fun onBackPressed() {
		setResult(Activity.RESULT_CANCELED, intent)
		super.onBackPressed()
	}

	override fun setListener() {
		super.setListener()

		RxView.clicks(tv_complete).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe {
			clipImage()
		}

		tv_cancel.setOnClickListener {
			onBackPressed()
		}

		iv_clip_image_rotation.setOnClickListener {
			if (mRotationDegree<=270){
				mRotationDegree = mRotationDegree+90;
			}else{
				mRotationDegree = 0
			}
			setImageAndClipParams()
		}
	}

	companion object {

		/**
		 * 计算最好的采样大小。
		 *
		 * @param origin 当前宽度
		 * @param target 限定宽度
		 * @return sampleSize
		 */
		private fun findBestSample(origin: Int, target: Int): Int {
			var sample = 1
			var out = origin / 2
			while (out > target) {
				sample *= 2
				out /= 2
			}
			return sample
		}

		/**
		 * 读取图片属性：旋转的角度
		 *
		 * @param path 图片绝对路径
		 * @return degree旋转的角度
		 */
		fun readPictureDegree(path: String?): Int {
			var degree = 0
			try {
				val exifInterface = ExifInterface(path)
				val orientation = exifInterface
						.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
				when (orientation) {
					ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
					ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
					ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
				}
			} catch (e: IOException) {
				e.printStackTrace()
			}

			return degree
		}
	}
}