package com.zxcx.zhizhe.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/11/6.
 * 启动广告右上角跳过控件
 */

public class WelcomeSkipView extends android.support.v7.widget.AppCompatTextView {
	
	public int countdown = 3;
	private String text = "跳过";
	private Paint backgroundPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Rect textRect = new Rect();
	private RectF oval = new RectF();
	private float sweepAngle;
	private ObjectAnimator animator = ObjectAnimator.ofFloat(this, "sweepAngle", -360, 0);
	private onFinishListener mListener;
	
	{
		backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.opacity_20));
		backgroundPaint.setAntiAlias(true);
		circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.button_blue));
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeWidth(ScreenUtils.dip2px(2));
		circlePaint.setAntiAlias(true);
		
		animator.setDuration(countdown * 1000);
		animator.setInterpolator(new LinearInterpolator());
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mListener.onAnimationFinish();
			}
		});
	}
	
	public WelcomeSkipView(Context context) {
		super(context);
	}
	
	public WelcomeSkipView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public WelcomeSkipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		animator.start();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		animator.removeAllListeners();
		animator.cancel();
	}
	
	public void stop() {
		animator.removeAllListeners();
		animator.cancel();
	}
	
	public void setListener(onFinishListener listener) {
		mListener = listener;
	}
	
	public void setCountdown(int countdown) {
		countdown = countdown;
	}
	
	@Keep
	@SuppressWarnings("unused")
	public void setSweepAngle(float sweepAngle) {
		this.sweepAngle = sweepAngle;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
			getMeasuredWidth() / 2 - ScreenUtils.dip2px(1.5f), backgroundPaint);
		oval.set(ScreenUtils.dip2px(1), ScreenUtils.dip2px(1), getMeasuredWidth() - 2,
			getMeasuredHeight() - 2);
		canvas.drawArc(oval, -90, sweepAngle, false, circlePaint);
		super.onDraw(canvas);
	}
	
	public interface onFinishListener {
		
		void onAnimationFinish();
	}
}
