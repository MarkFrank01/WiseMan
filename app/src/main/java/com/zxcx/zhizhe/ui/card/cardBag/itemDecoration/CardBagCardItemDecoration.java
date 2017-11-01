package com.zxcx.zhizhe.ui.card.cardBag.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class CardBagCardItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(12f);
    private int bottomSpace = ScreenUtils.dip2px(15f);
    private int space = (int) (ScreenUtils.dip2px(15f)/2);

    public CardBagCardItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // slp.getSpanIndex(): 这个可以拿到它在同一行排序的真实顺序
        if (slp.getSpanIndex() == 0) {
            outRect.left = defultSpace;
            outRect.right = space;
        } else {
            outRect.left = space;
            outRect.right = defultSpace;
        }

        if (parent.getChildAdapterPosition(view) < 2){
            outRect.top = ScreenUtils.dip2px(18f);
        }
    }
}
