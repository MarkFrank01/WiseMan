package com.zxcx.zhizhe.ui.my.selectAttention;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class SelectAttentionItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(12f);
    private int bottomSpace = ScreenUtils.dip2px(15f);
    int space = (int) (ScreenUtils.dip2px(12f)/2);

    public SelectAttentionItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (parent.getChildAdapterPosition(view)){
            case 0:
                outRect.left = ScreenUtils.dip2px(40f);
                break;
            case 1:
                outRect.left = ScreenUtils.dip2px(20f);
                break;
            case 2:
                break;
            case 3:
                outRect.left = ScreenUtils.dip2px(20f);
                break;
            case 4:
                outRect.left = ScreenUtils.dip2px(40f);
                break;
        }
    }
}
