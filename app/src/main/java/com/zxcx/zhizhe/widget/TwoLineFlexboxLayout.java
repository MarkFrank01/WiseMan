package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by anm on 2017/11/20.
 */

public class TwoLineFlexboxLayout extends FlexboxLayout {
    public TwoLineFlexboxLayout(Context context) {
        super(context);
    }

    public TwoLineFlexboxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoLineFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getFlexLines().size()>2){
            removeViewAt(getChildCount()-1);
        }
    }
}
