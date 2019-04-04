package com.zxcx.zhizhe.utils

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.TouchDelegate
import android.view.View
import android.widget.EditText
import java.text.DecimalFormat


/**
 * Created by anm on 2018/2/26.
 * Kotlin拓展函数工具
 */
fun <T> Context?.startActivity(clazz: Class<T>, action: (intent: Intent) -> Unit) {
	if (this == null) {
		return
	}
	val intent = Intent(this, clazz)
	action(intent)
	this.startActivity(intent)
}

fun Context.getColorForKotlin(@ColorRes resId: Int): Int {
	return ContextCompat.getColor(this, resId)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
	this.addTextChangedListener(object : TextWatcher {
		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		}

		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		}

		override fun afterTextChanged(editable: Editable?) {
			afterTextChanged.invoke(editable.toString())
		}
	})
}

/**
 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
 *
 * @param view
 * @param top
 * @param bottom
 * @param left
 * @param right
 */
fun View.expandViewTouchDelegate(top: Int, bottom: Int, left: Int, right: Int) {

	(this.parent as View).post {
		val bounds = Rect()
		this.isEnabled = true
		this.getHitRect(bounds)

		bounds.top -= top
		bounds.bottom += bottom
		bounds.left -= left
		bounds.right += right

		val touchDelegate = TouchDelegate(bounds, this)

		if (View::class.java.isInstance(this.parent)) {
			(this.parent as View).touchDelegate = touchDelegate
		}
	}
}

/**
 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
 *
 * @param view
 * @param size
 */
fun View.expandViewTouchDelegate(size: Int) {

	(this.parent as View).post {
		val bounds = Rect()
		this.isEnabled = true
		this.getHitRect(bounds)

		bounds.top -= size
		bounds.bottom += size
		bounds.left -= size
		bounds.right += size

		val touchDelegate = TouchDelegate(bounds, this)

		if (View::class.java.isInstance(this.parent)) {
			(this.parent as View).touchDelegate = touchDelegate
		}
	}
}

/**
 * 还原View的触摸和点击响应范围,最小不小于View自身范围
 *
 * @param view
 */
fun View.restoreViewTouchDelegate() {

	(this.parent as View).post {
		val bounds = Rect()
		bounds.setEmpty()
		val touchDelegate = TouchDelegate(bounds, this)

		if (View::class.java.isInstance(this.parent)) {
			(this.parent as View).touchDelegate = touchDelegate
		}
	}
}

fun Int.getFormatNumber(): String {
	if (this < 1000) {
		return this.toString()
	} else if (this < 10000) {
		val dou = this / 1000.00
		val df = DecimalFormat("#.#")
		return df.format(dou) + "k"
	} else {
		val dou = this / 10000.00
		val df = DecimalFormat("#.#")
		return df.format(dou) + "w"
	}
}

/**
 * String转成Int
 */
fun String?.parseInt(defaultValue: Int = 0): Int {
    if (this.isNullOrEmpty()) return defaultValue
    return tryCatch(defaultValue) {
        this!!.toInt()
    }
}

/**
 * String转成Double
 */
fun String?.parseDouble(defaultValue: Double = 0.0): Double {
    if (this.isNullOrEmpty()) return defaultValue
    return tryCatch(defaultValue) {
        this!!.toDouble()
    }
}

/**
 * String转成Float
 */
fun String?.parseFloat(defaultValue: Float = 0f): Float {
    if (this.isNullOrEmpty()) return defaultValue
    return tryCatch(defaultValue) {
        this!!.toFloat()
    }
}

/**
 * String转成Long
 */
fun String?.parseLong(defaultValue: Long = 0): Long {
    if (this.isNullOrEmpty()) return defaultValue
    return tryCatch(defaultValue) {
        this!!.toLong()
    }
}

/**
 * 当需要返回值try的时候使用
 */
inline fun <T> tryCatch(default: T, block: () -> T): T {
    return try {
        block()
    } catch (_: Exception) {
        default
    }
}
