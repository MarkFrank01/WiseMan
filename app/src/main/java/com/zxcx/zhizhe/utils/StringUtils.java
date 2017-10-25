package com.zxcx.zhizhe.utils;

import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.R;

/**
 * Created by anm on 2017/6/12.
 */

public class StringUtils {

    public static boolean isEmpty(String s){
        if (s == null || s.isEmpty() || s.length() == 0 || "".equals(s)){
            return true;
        }else {
            return false;
        }
    }

    public static String getString(@StringRes int id){
        return App.getContext().getString(id);
    }

    public static void setTextviewColorAndBold(TextView textView, String key, String value) {
        if (isEmpty(value)) {
            return;
        }
        if (!isEmpty(key)) {
            SpannableStringBuilder style = new SpannableStringBuilder(value);
            int index = value.indexOf(key);
            if (index >= 0) {
                while (index < value.length() && index >= 0) {
                    style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(App.getContext(), R.color.button_blue)), index, index + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(style);
                    index = value.indexOf(key, index + key.length());
                }
            } else {
                textView.setText(value);
            }

        } else {
            textView.setText(value);
        }
    }

}
