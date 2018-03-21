package com.zxcx.zhizhe.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.parseColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import com.makeramen.roundedimageview.RoundedImageView
import com.zxcx.zhizhe.R



/**
 * Created by anm on 2018/3/20.
 */

class CardRoundedImageView(context: Context, attrs: AttributeSet) : RoundedImageView(context, attrs) {

    private var widthWeight: Int
    private var heightWeight: Int
    private var hasShade: Boolean
    private var radius: Int
    private var paint: Paint = Paint()
    private var rectF: RectF = RectF()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CardImageView)
        widthWeight = a.getInt(R.styleable.CardImageView_widthWeight, -1)
        heightWeight = a.getInt(R.styleable.CardImageView_heightWeight, -1)
        hasShade = a.getBoolean(R.styleable.CardImageView_hasShade,false)
        radius = a.getDimensionPixelSize(R.styleable.CardImageView_radius, -1)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(widthWeight != -1 && heightWeight != -1) {
            val w = this.measuredWidth
            val h = w * heightWeight / widthWeight
            setMeasuredDimension(w + paddingLeft + paddingRight, h + paddingTop + paddingBottom)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (hasShade) {
            val shader = LinearGradient(0f, 0f, 0f, h.toFloat(), parseColor("#00000000"),
                    parseColor("#66000000"), TileMode.CLAMP)
            paint.shader = shader

            rectF.set(0f, 0f, w.toFloat(), h.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (hasShade && radius != -1) {
            canvas?.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)
        }
    }
}
