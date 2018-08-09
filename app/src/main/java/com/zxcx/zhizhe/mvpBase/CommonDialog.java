package com.zxcx.zhizhe.mvpBase;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2018/3/15.
 * 样式相同的通用Dialog封装
 */

public class CommonDialog extends BaseDialog {
	
	/**
	 * 居中，左右边距53dp，背景透明
	 */
	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			Window window = getDialog().getWindow();
			window.setBackgroundDrawableResource(R.color.translate);
			window.getDecorView()
				.setPadding(ScreenUtils.dip2px(52f), 0, ScreenUtils.dip2px(52f), 0);
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.gravity = Gravity.CENTER;
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			window.setAttributes(lp);
		}
	}
}
