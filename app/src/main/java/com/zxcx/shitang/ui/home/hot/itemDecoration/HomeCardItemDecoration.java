package com.zxcx.shitang.ui.home.hot.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.shitang.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class HomeCardItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(12f);
    int space = (int) (ScreenUtils.dip2px(15f)/2);

    public HomeCardItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0){
            if (parent.getChildLayoutPosition(view) % 2 == 1) {
                outRect.left = defultSpace;
                outRect.right = space;
            } else {
                outRect.left = space;
                outRect.right = defultSpace;
            }
        }
    }
}
