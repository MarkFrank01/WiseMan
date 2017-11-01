package com.zxcx.zhizhe.ui.home.hot.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class HomeCardItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(12f);
    private int bottomSpace = ScreenUtils.dip2px(15f);
    int space = (int) (ScreenUtils.dip2px(12f)/2);

    public HomeCardItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0){
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            // slp.getSpanIndex(): 这个可以拿到它在同一行排序的真实顺序
            if (slp.getSpanIndex() == 0) {
                outRect.left = defultSpace;
                outRect.right = space;
            } else {
                outRect.left = space;
                outRect.right = defultSpace;
            }
        }
    }
}
