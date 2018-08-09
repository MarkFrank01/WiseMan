package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

/**
 * 长文详情网络错误占位页
 */

public class CardDetailsNetworkErrorCallback extends Callback {
	
	@Override
	protected int onCreateView() {
		return R.layout.view_network_error;
	}
}
