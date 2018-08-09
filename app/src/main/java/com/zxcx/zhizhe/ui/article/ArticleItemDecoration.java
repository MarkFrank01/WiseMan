package com.zxcx.zhizhe.ui.article;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2017/6/20.
 * 长文列表间隔
 */

public class ArticleItemDecoration extends RecyclerView.ItemDecoration {
	
	public ArticleItemDecoration() {
		super();
	}
	
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
		RecyclerView.State state) {
		if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
			outRect.bottom = ScreenUtils.dip2px(6);
		}
	}
}
