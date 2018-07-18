package com.zxcx.zhizhe.ui.my.readCards;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 */

public class MyCardItemDecoration extends RecyclerView.ItemDecoration {
	
	private int defultSpace = ScreenUtils.dip2px(15f);
	
	public MyCardItemDecoration() {
		super();
	}
	
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
		RecyclerView.State state) {
		int position = parent.getChildAdapterPosition(view);
		if (position == 0) {
			outRect.top = defultSpace;
		}
	}
}
