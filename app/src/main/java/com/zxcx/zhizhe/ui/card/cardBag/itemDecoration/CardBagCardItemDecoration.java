package com.zxcx.zhizhe.ui.card.cardBag.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
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

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = defultSpace;
            outRect.right = space;
        } else {
            outRect.left = space;
            outRect.right = defultSpace;
        }

        if (parent.getChildAdapterPosition(view) < 2){
            outRect.top = ScreenUtils.dip2px(18f);
        }

        if (parent.getAdapter().getItemCount() %2 == 1) {
            if (parent.getChildAdapterPosition(view) < (parent.getAdapter().getItemCount()-3)){
                outRect.bottom = bottomSpace;
            }
        } else {
            if (parent.getChildAdapterPosition(view) < (parent.getAdapter().getItemCount()-2)){
                outRect.bottom = bottomSpace;
            }
        }
    }
}
