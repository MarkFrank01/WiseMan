package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/12/12.
 * 圆角12dp
 */

public class RoundedFrameLayout extends FrameLayout {
	
	private final Paint imagePaint;
	int radius = ScreenUtils.dip2px(12);
	Path path = new Path();
	RectF mRectF = new RectF();
	Paint roundPaint = new Paint();
	
	{
		roundPaint.setColor(Color.WHITE);
		roundPaint.setAntiAlias(true);
		roundPaint.setStyle(Paint.Style.FILL);
		roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		
		imagePaint = new Paint();
		imagePaint.setXfermode(null);
	}
	
	public RoundedFrameLayout(@NonNull Context context) {
		super(context);
	}
	
	public RoundedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public RoundedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
		int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), imagePaint,
			Canvas.ALL_SAVE_FLAG);
		super.dispatchDraw(canvas);
		drawTopLeft(canvas);//用PorterDuffXfermode
		drawTopRight(canvas);//用PorterDuffXfermode
		drawBottomLeft(canvas);//用PorterDuffXfermode
		drawBottomRight(canvas);//用PorterDuffXfermode
		canvas.restore();
	}
	
	private void drawTopLeft(Canvas canvas) {
		if (radius > 0) {
			Path path = new Path();
			path.moveTo(0, radius);
			path.lineTo(0, 0);
			path.lineTo(radius, 0);
			path.arcTo(new RectF(0, 0, radius * 2, radius * 2),
				-90, -90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}
	
	private void drawTopRight(Canvas canvas) {
		if (radius > 0) {
			int width = getWidth();
			Path path = new Path();
			path.moveTo(width - radius, 0);
			path.lineTo(width, 0);
			path.lineTo(width, radius);
			path.arcTo(new RectF(width - 2 * radius, 0, width,
				radius * 2), 0, -90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}
	
	private void drawBottomLeft(Canvas canvas) {
		if (radius > 0) {
			int height = getHeight();
			Path path = new Path();
			path.moveTo(0, height - radius);
			path.lineTo(0, height);
			path.lineTo(radius, height);
			path.arcTo(new RectF(0, height - 2 * radius,
				radius * 2, height), 90, 90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}
	
	private void drawBottomRight(Canvas canvas) {
		if (radius > 0) {
			int height = getHeight();
			int width = getWidth();
			Path path = new Path();
			path.moveTo(width - radius, height);
			path.lineTo(width, height);
			path.lineTo(width, height - radius);
			path.arcTo(new RectF(width - 2 * radius, height - 2
				* radius, width, height), 0, 90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}
}
