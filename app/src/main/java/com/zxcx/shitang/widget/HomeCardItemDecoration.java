package com.zxcx.shitang.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.shitang.App;
import com.zxcx.shitang.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class HomeCardItemDecoration extends RecyclerView.ItemDecoration {

    private int defultSpace = ScreenUtils.dip2px(App.getContext(),7.5f);

    public HomeCardItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view)%2 == 0){
            outRect.right = defultSpace;
        }else {
            outRect.left = defultSpace;
        }
    }
}
