package com.zxcx.zhizhe.widget.centerpopup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/4/17
 * @Description :
 */
public class InviteCenterPopup extends CenterPopupView{

    private OnSelectListener mSelectListener;

    public InviteCenterPopup(@NonNull Context context) {
        super(context);
    }

    public InviteCenterPopup(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        mSelectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.invite_center_popup;
    }
}
