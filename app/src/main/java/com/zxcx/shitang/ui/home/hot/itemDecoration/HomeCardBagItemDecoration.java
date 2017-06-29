package com.zxcx.shitang.ui.home.hot.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.shitang.App;
import com.zxcx.shitang.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class HomeCardBagItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(App.getContext(),6f);

    public HomeCardBagItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0){
            outRect.left = ScreenUtils.dip2px(App.getContext(),12f);
            outRect.right = defultSpace;
        }else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() -1){
            outRect.right = ScreenUtils.dip2px(App.getContext(),12f);
        }else {
            outRect.right = defultSpace;
        }
    }
}
