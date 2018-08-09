package com.zxcx.zhizhe.loadCallback;

import com.kingja.loadsir.callback.Callback;
import com.zxcx.zhizhe.R;

/**
 * 关注页需要登录占位页
 */

public class AttentionNeedLoginCallback extends Callback {
	
	@Override
	protected int onCreateView() {
		return R.layout.view_attention_need_login;
	}
}
