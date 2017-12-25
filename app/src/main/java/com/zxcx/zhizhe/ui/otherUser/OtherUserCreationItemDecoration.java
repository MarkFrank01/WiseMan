package com.zxcx.zhizhe.ui.otherUser;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class OtherUserCreationItemDecoration extends RecyclerView.ItemDecoration {

    private int defaultSpace = ScreenUtils.dip2px(20f);

    public OtherUserCreationItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = defaultSpace;
        }
    }
}
