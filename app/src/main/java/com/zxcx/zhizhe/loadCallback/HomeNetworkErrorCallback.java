package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

/**
 * 首页网络错误占位页
 */

public class HomeNetworkErrorCallback extends Callback {
	
	@Override
	protected int onCreateView() {
		return R.layout.view_home_network_error;
	}
	
}
