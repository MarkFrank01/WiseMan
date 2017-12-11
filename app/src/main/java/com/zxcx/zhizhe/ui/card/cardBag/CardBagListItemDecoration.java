package com.zxcx.zhizhe.ui.card.cardBag;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class CardBagListItemDecoration extends RecyclerView.ItemDecoration {

    public CardBagListItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0){
            outRect.top = ScreenUtils.dip2px(8);
        }
    }
}
