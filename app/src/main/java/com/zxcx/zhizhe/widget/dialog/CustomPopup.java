package com.zxcx.zhizhe.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/1/29
 * @Description :
 */
public class CustomPopup extends BottomPopupView {

    public CustomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_popup;
    }
}
