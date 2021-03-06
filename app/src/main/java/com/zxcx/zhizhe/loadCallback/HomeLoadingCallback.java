package com.zxcx.zhizhe.loadCallback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

/**
 * 首页加载占位页
 */

public class HomeLoadingCallback extends Callback {
	
	private ImageView imageView;
	
	@Override
	protected int onCreateView() {
		return R.layout.layout_home_loading;
	}
	
	@Override
	public void onAttach(Context context, View view) {
		super.onAttach(context, view);
		imageView = (ImageView) view.findViewById(R.id.iv_loading);
//		((AnimationDrawable) imageView.getDrawable()).start();
	}
	
	@Override
	public void onDetach() {
//		((AnimationDrawable) imageView.getDrawable()).stop();
		super.onDetach();
	}
}
