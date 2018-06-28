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


/**
 * Created by anm on 2018/2/26.
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