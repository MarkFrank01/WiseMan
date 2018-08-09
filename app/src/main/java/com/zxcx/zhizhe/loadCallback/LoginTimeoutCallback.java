package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

/**
 * 登录超时占位页
 */

public class LoginTimeoutCallback extends Callback {
	
	@Override
	protected int onCreateView() {
		return R.layout.view_login_timeout;
	}
	
}
