package com.zxcx.shitang.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anm on 2017/6/27.
 */

public class HomeSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mScrollUpChild;

    public HomeSwipeRefreshLayout(Context context) {
        super(context);
    }

    public HomeSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
//            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
            return mScrollUpChild.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }

}
