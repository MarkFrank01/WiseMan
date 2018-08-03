package com.zxcx.zhizhe.widget


import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.zxcx.zhizhe.R

class AttentionLoadMoreView : LoadMoreView() {

	lateinit var mListener: () -> Unit

	override fun getLayoutId(): Int {
		return R.layout.view_attention_load_more
	}

	override fun getLoadingViewId(): Int {
		return R.id.load_more_loading_view
	}

	override fun getLoadFailViewId(): Int {
		return R.id.load_more_load_fail_view
	}

	override fun getLoadEndViewId(): Int {
		return R.id.load_more_load_end_view
	}

	override fun convert(holder: BaseViewHolder) {
		super.convert(holder)
		holder.getView<FrameLayout>(R.id.load_more_load_end_view).setOnClickListener {
			mListener.invoke()
		}
	}
}
