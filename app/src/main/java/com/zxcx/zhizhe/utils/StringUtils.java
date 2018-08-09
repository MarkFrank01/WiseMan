package com.zxcx.zhizhe.utils;

import android.support.annotation.StringRes;
import com.zxcx.zhizhe.App;

/**
 * Created by anm on 2017/6/12.
 */

public class StringUtils {
	
	public static boolean isEmpty(String s) {
		if (s == null || s.isEmpty() || s.length() == 0 || "".equals(s)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getString(@StringRes int id) {
		return App.getContext().getString(id);
	}
	
}
