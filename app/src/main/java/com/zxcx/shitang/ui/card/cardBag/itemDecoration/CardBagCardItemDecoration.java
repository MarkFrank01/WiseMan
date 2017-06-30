package com.zxcx.shitang.ui.card.cardBag.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.shitang.App;
import com.zxcx.shitang.R;
import com.zxcx.shitang.utils.ScreenUtils;

import static com.zxcx.shitang.App.getContext;

/**
 * Created by anm on 2017/6/20.
 */

public class CardBagCardItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(getContext(),12f);
    int screenWidth = ScreenUtils.getScreenWidth(getContext()); //屏幕宽度
    float x = getContext().getResources().getDimension(R.dimen.dp_168);
    int space = (int) ((screenWidth - defultSpace*2 - x*2)/2);

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


        if (parent.getChildLayoutPosition(view) < 2){
            outRect.top = ScreenUtils.dip2px(App.getContext(), 18f);
        }
    }
}
