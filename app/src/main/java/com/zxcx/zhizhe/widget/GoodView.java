/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 点赞效果
 *
 * @author venshine
 */
public class GoodView extends PopupWindow {


	private int DISTANCE = 60;   // 默认移动距离

	private int FROM_Y_DELTA = 0; // Y轴移动起始偏移量

	private int TO_Y_DELTA = DISTANCE; // Y轴移动最终偏移量

	private float FROM_ALPHA = 1.0f;    // 起始时透明度

	private float TO_ALPHA = 0.0f;  // 结束时透明度

	private int DURATION = 800; // 动画时长

	private String TEXT = ""; // 默认文本

	private int TEXT_SIZE = 12; // 默认文本字体大小

	private int TEXT_COLOR = Color.BLACK;   // 默认文本字体颜色

	private String mText = TEXT;

	private int mTextColor = TEXT_COLOR;

	private int mTextSize = TEXT_SIZE;

	private int mFromY = FROM_Y_DELTA;

	private int mToY = TO_Y_DELTA;

	private float mFromAlpha = FROM_ALPHA;

	private float mToAlpha = TO_ALPHA;

	private int mDuration = DURATION;

	private int mDistance = DISTANCE;

	private AnimationSet mAnimationSet;

	private boolean mChanged = false;

	private Context mContext = null;

	private TextView mGood = null;

	public GoodView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	private static int getTextViewHeight(TextView textView, int width) {
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		textView.measure(widthMeasureSpec, heightMeasureSpec);
		return textView.getMeasuredHeight();
	}

	private void initView() {
		RelativeLayout layout = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams params =
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		mGood = new TextView(mContext);
		mGood.setIncludeFontPadding(false);
		mGood.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
		mGood.setTextColor(mTextColor);
		mGood.setText(mText);
		mGood.setLayoutParams(params);
		layout.addView(mGood);
		setContentView(layout);

		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mGood.measure(w, h);
		setWidth(mGood.getMeasuredWidth());
		setHeight(mDistance + mGood.getMeasuredHeight());
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setFocusable(false);
		setTouchable(false);
		setOutsideTouchable(false);

		mAnimationSet = createAnimation();
	}

	/**
	 * 设置文本
	 */
	public void setText(String text) {
		if (TextUtils.isEmpty(text)) {
			throw new IllegalArgumentException("text cannot be null.");
		}
		mText = text;
		mGood.setText(text);
		mGood.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int w = (int) mGood.getPaint().measureText(text);
		setWidth(w);
		setHeight(mDistance + getTextViewHeight(mGood, w));
	}

	/**
	 * 设置文本颜色
	 */
	public void setTextColor(int color) {
		mTextColor = color;
		mGood.setTextColor(color);
	}

	/**
	 * 设置文本大小
	 */
	public void setTextSize(int textSize) {
		mTextSize = textSize;
		mGood.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
	}

	/**
	 * 设置文本信息
	 */
	public void setTextInfo(String text, int textColor, int textSize) {
		setTextColor(textColor);
		setTextSize(textSize);
		setText(text);
	}

	/**
	 * 设置图片
	 */
	public void setImage(int resId) {
		setImage(mContext.getResources().getDrawable(resId));
	}

	/**
	 * 设置图片
	 */
	public void setImage(Drawable drawable) {
		if (drawable == null) {
			throw new IllegalArgumentException("drawable cannot be null.");
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mGood.setBackground(drawable);
		} else {
			mGood.setBackgroundDrawable(drawable);
		}
		mGood.setText("");
		setWidth(drawable.getIntrinsicWidth());
		setHeight(mDistance + drawable.getIntrinsicHeight());
	}

	/**
	 * 设置移动距离
	 */
	public void setDistance(int dis) {
		mDistance = dis;
		mToY = dis;
		mChanged = true;
		setHeight(mDistance + mGood.getMeasuredHeight());
	}

	/**
	 * 设置Y轴移动属性
	 */
	public void setTranslateY(int fromY, int toY) {
		mFromY = fromY;
		mToY = toY;
		mChanged = true;
	}

	/**
	 * 设置透明度属性
	 */
	public void setAlpha(float fromAlpha, float toAlpha) {
		mFromAlpha = fromAlpha;
		mToAlpha = toAlpha;
		mChanged = true;
	}

	/**
	 * 设置动画时长
	 */
	public void setDuration(int duration) {
		mDuration = duration;
		mChanged = true;
	}

	/**
	 * 重置属性
	 */
	public void reset() {
		mText = TEXT;
		mTextColor = TEXT_COLOR;
		mTextSize = TEXT_SIZE;
		mFromY = FROM_Y_DELTA;
		mToY = TO_Y_DELTA;
		mFromAlpha = FROM_ALPHA;
		mToAlpha = TO_ALPHA;
		mDuration = DURATION;
		mDistance = DISTANCE;
		mChanged = false;
		mAnimationSet = createAnimation();
	}

	/**
	 * 展示
	 */
	public void show(View v) {
		if (!isShowing()) {
			try {
				int offsetY = -v.getHeight() - getHeight();
				showAsDropDown(v, v.getWidth() / 2 - getWidth() / 2, offsetY);
				if (mAnimationSet == null || mChanged) {
					mAnimationSet = createAnimation();
					mChanged = false;
				}
				mGood.startAnimation(mAnimationSet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 动画
	 */
	private AnimationSet createAnimation() {
		mAnimationSet = new AnimationSet(true);
		TranslateAnimation translateAnim = new TranslateAnimation(0, 0, mFromY, -mToY);
		AlphaAnimation alphaAnim = new AlphaAnimation(mFromAlpha, mToAlpha);
		mAnimationSet.addAnimation(translateAnim);
		mAnimationSet.addAnimation(alphaAnim);
		mAnimationSet.setDuration(mDuration);
		mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isShowing()) {
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							dismiss();
						}
					});
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		return mAnimationSet;
	}
}
