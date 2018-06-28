package com.zxcx.zhizhe.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.parseColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader.TileMode
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.zxcx.zhizhe.R


/**
 * Created by anm on 2018/3/20.
 */

class CardImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

	private var widthWeight: Int
	private var heightWeight: Int
	private var hasShade: Boolean
	var paint: Paint = Paint()

	init {
		val a = context.obtainStyledAttributes(attrs, R.styleable.CardImageView)
		widthWeight = a.getInt(R.styleable.CardImageView_widthWeight, -1)
		heightWeight = a.getInt(R.styleable.CardImageView_heightWeight, -1)
		hasShade = a.getBoolean(R.styleable.CardImageView_hasShade, false)
		a.recycle()
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		if (widthWeight != -1 && heightWeight != -1) {
			val w = this.measuredWidth
			val h = w * heightWeight / widthWeight
			setMeasuredDimension(w + paddingLeft + paddingRight, h + paddingTop + paddingBottom)
		}
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		if (widthWeight != -1 && heightWeight != -1) {
			val shader = LinearGradient(0f, 0f, 0f, h.toFloat(), parseColor("#00000000"),
					parseColor("#66000000"), TileMode.CLAMP)
			paint.shader = shader
		}
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		if (hasShade) {
			canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
		}
	}
}
