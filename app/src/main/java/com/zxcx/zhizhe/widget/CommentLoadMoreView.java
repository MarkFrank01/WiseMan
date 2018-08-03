package com.zxcx.zhizhe.widget;


import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.zxcx.zhizhe.R;

public final class CommentLoadMoreView extends LoadMoreView {
	
	@Override
	public int getLayoutId() {
		return R.layout.view_comment_load_more;
	}
	
	@Override
	protected int getLoadingViewId() {
		return R.id.load_more_loading_view;
	}
	
	@Override
	protected int getLoadFailViewId() {
		return R.id.load_more_load_fail_view;
	}
	
	@Override
	protected int getLoadEndViewId() {
		return R.id.load_more_load_end_view;
	}
}
