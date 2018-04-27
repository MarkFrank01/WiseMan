package com.zxcx.zhizhe.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.utils.ScreenUtils;

public class LoadingLayout extends LinearLayout {

    int[] position = new int[2];

    public LoadingLayout(Context context) {
        super(context);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getLocationOnScreen(position);
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = ScreenUtils.getScreenWidth();
        if (getContext() instanceof MainActivity){
            lp.height = ScreenUtils.getScreenHeight() - position[1] - ScreenUtils.dip2px(50);
        }else {
            lp.height = ScreenUtils.getScreenHeight() - position[1];
        }
        setLayoutParams(lp);
    }
}
