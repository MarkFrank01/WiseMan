package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;
import java.util.ArrayList;

/**
 * Created by anm on 2017/12/27.
 * 字号选择控件
 */

public class CustomSeekBar extends View {
	
	private int width;
	private int height;
	private int downX = 0;
	private int downY = 0;
	private int upX = 0;
	private int upY = 0;
	private int moveX = 0;
	private int moveY = 0;
	private int perWidth = 0;
	private Paint mPaint;
	private Paint mTextPaint;
	private Paint buttonPaint;
	private Bitmap thumb;
	private int cur_sections = 0;
	private ResponseOnTouch responseOnTouch;
	private int bitMapHeight = 11;//第一个点的起始位置起始，图片的长宽是76，所以取一半的距离
	private int textWidth;
	private ArrayList<String> section_title;
	
	public CustomSeekBar(Context context) {
		super(context);
	}
	
	public CustomSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		cur_sections = 0;
		thumb = BitmapFactory.decodeResource(getResources(), R.drawable.thumb);   //这个是滑动图标
		bitMapHeight = thumb.getHeight() / 2;   //这里影响点中的图标的位置  这个正好 不用改
		int textSize = (int) TypedValue
			.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
		mPaint = new Paint(Paint.DITHER_FLAG);
		mPaint.setColor(ContextCompat.getColor(getContext(), R.color.line));
		mPaint.setAntiAlias(true);//锯齿不显示
		mPaint.setStrokeWidth(ScreenUtils.dip2px(1));
		mTextPaint = new Paint(Paint.DITHER_FLAG);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(textSize);
		Rect rect = new Rect();
		mTextPaint.getTextBounds("默认", 0, 2, rect);
		textWidth = rect.width();
		buttonPaint = new Paint(Paint.DITHER_FLAG);
		buttonPaint.setAntiAlias(true);
		
	}
	
	/**
	 * 实例化后调用，设置bar的段数和文字
	 */
	public void initData(ArrayList<String> section) {
		if (section != null) {
			section_title = section;
		} else {
			//如果没有传入正确的分类级别数据，则默认使用“低”“中”“高”
			String[] str = new String[]{"低", "中", "高"};
			section_title = new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {
				section_title.add(str[i]);
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		width = widthSize;
		//控件的高度
		//height = 185;
		height = (int) TypedValue
			.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
		setMeasuredDimension(width, height);
		perWidth = (width - textWidth) / (section_title.size() - 1);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(textWidth / 2, height / 3, width - textWidth / 2, height / 3, mPaint);
		int section = 0;
		while (section < section_title.size()) {
			canvas
				.drawLine(textWidth / 2 + perWidth * section, 0, textWidth / 2 + perWidth * section,
					height / 3, mPaint);
			if (cur_sections == section) {
				mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.button_blue));
			} else {
				mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color_2));
			}
			canvas
				.drawText(section_title.get(section), perWidth * section,
					height - ScreenUtils.dip2px(2),
					mTextPaint);
			section++;
		}
		canvas.drawBitmap(thumb, textWidth / 2 + perWidth * cur_sections - thumb.getWidth() / 2,
			height / 3 - thumb.getHeight() / 2, buttonPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.img_setting_seekbar_thumbe_large);
				downX = (int) event.getX();
				downY = (int) event.getY();
				responseTouch(downX, downY);
				break;
			case MotionEvent.ACTION_MOVE:
				//thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.img_setting_seekbar_thumbe_large);
				moveX = (int) event.getX();
				moveY = (int) event.getY();
				responseTouch(moveX, moveY);
				break;
			case MotionEvent.ACTION_UP:
				//thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.img_setting_seekbar_thumbe_large);
				upX = (int) event.getX();
				upY = (int) event.getY();
				responseTouch(upX, upY);
				responseOnTouch.onTouchResponse(cur_sections);
				break;
		}
		return true;
	}
	
	private void responseTouch(int x, int y) {
		if (x <= width - bitMapHeight / 2) {
			cur_sections = (x + perWidth / 3) / perWidth;
		} else {
			cur_sections = section_title.size() - 1;
		}
		invalidate();
	}
	
	//设置监听
	public void setResponseOnTouch(ResponseOnTouch response) {
		//注意 ，这里是接口，实现你到达界点的监听事件，因为这个自定义控件继承的View而不是SeekBar，所以只能使用接口实现监听
		responseOnTouch = response;
	}
	
	//设置进度
	public void setProgress(int progress) {
		cur_sections = progress;
		invalidate();
	}
	
	
	public interface ResponseOnTouch {
		
		public void onTouchResponse(int volume);
	}
}
