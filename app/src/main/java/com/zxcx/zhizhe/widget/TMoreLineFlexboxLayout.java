package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by anm on 2017/11/20.
 * 两行标签控件
 */

public class TMoreLineFlexboxLayout extends FlexboxLayout {

	public TMoreLineFlexboxLayout(Context context) {
		super(context);
	}

	public TMoreLineFlexboxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TMoreLineFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		if (getFlexLines().size() > 2) {
//			removeViewAt(getChildCount() - 1);
//		}
	}
}
