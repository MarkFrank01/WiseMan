package com.zxcx.zhizhe.ui.classify;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class ClassifyItemDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace = ScreenUtils.dip2px(15f);

    public ClassifyItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
        int childCount = parent.getAdapter().getItemCount();
        if (pos >= childCount - 1)// 如果是最后一个，则需要绘制底部
            outRect.bottom = bottomSpace;
    }
}
