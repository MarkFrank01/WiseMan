package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/11/6.
 */

public class RoundProgressBar extends View {
	
	private Paint backgroundPaint = new Paint();
	private Paint circlePaint = new Paint();
	private RectF oval = new RectF();
	private float sweepAngle;
	private int strokeWidth = ScreenUtils.dip2px(5);
	
	{
		backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.opacity_30_blue));
		backgroundPaint.setStyle(Paint.Style.STROKE);
		backgroundPaint.setStrokeWidth(strokeWidth);
		backgroundPaint.setAntiAlias(true);
		circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.button_blue));
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeCap(Paint.Cap.ROUND);
		circlePaint.setStrokeWidth(strokeWidth);
		circlePaint.setAntiAlias(true);
	}
	
	public RoundProgressBar(Context context) {
		super(context);
	}
	
	public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Keep
	@SuppressWarnings("unused")
	public void setProgress(float progress) {
		this.sweepAngle = progress * 360 / 100;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
			getMeasuredWidth() / 2 - ScreenUtils.dip2px(5), backgroundPaint);
		oval.set(ScreenUtils.dip2px(5), ScreenUtils.dip2px(5),
			getMeasuredWidth() - ScreenUtils.dip2px(5),
			getMeasuredHeight() - ScreenUtils.dip2px(5));
		canvas.drawArc(oval, -90, sweepAngle, false, circlePaint);
		super.onDraw(canvas);
	}
}
