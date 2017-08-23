package com.zxcx.shitang.ui.classify.itemDecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.App;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.classify.ClassifyBean;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/20.
 */

public class ClassifyItemDecoration extends RecyclerView.ItemDecoration {

    private int space = ScreenUtils.dip2px(21);
    private int dividerHeight = ScreenUtils.dip2px(10);
    private List<MultiItemEntity> mList;
    private Paint mPaint;

    public ClassifyItemDecoration(List<MultiItemEntity> list) {
        super();
        mList = list;
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(App.getContext(), R.color.fault));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //获取在RecyclerView中的实际位置
            int position = parent.getChildAdapterPosition(child);
            if (mList.get(position).getItemType() == ClassifyBean.TYPE_CLASSIFY){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                // divider的top 应该是 item的bottom 加上 marginBottom 再加上 Y方向上的位移,间隔
                int bottom = (int) (child.getTop() + params.topMargin);
                // divider的bottom就是top减去divider的高度了
                int top = bottom - dividerHeight;
                c.drawRect(0, top, parent.getWidth(), bottom, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position != 0){
            if (mList.get(position).getItemType() == ClassifyBean.TYPE_CLASSIFY){
                outRect.top = space;
            }
        }
    }
}
