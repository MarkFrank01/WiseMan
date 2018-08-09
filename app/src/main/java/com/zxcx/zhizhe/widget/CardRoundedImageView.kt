package com.zxcx.zhizhe.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R


/**
 * Created by anm on 2018/3/20.
 */

class CardRoundedImageView(context: Context, attrs: AttributeSet) : RoundedImageView(context, attrs) {

	private var hasShade: Boolean
	private var paint: Paint = Paint()
	private var rect: Rect = Rect()
	private var shade: Drawable

	init {
		val a = context.obtainStyledAttributes(attrs, R.styleable.CardImageView)
		hasShade = a.getBoolean(R.styleable.CardImageView_hasShade, false)
		shade = a.getDrawable(R.styleable.CardImageView_shade)
		a.recycle()
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		rect.set(0, 0, w, h)
		shade.bounds = rect
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		if (hasShade) {
			canvas?.let { shade.draw(canvas) }
		}
	}
}
