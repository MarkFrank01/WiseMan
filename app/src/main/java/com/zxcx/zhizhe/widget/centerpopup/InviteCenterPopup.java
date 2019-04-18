package com.zxcx.zhizhe.widget.centerpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.widget.VerificationCodeInput;

/**
 * @author : MarkFrank01
 * @Created on 2019/4/17
 * @Description :
 */
public class InviteCenterPopup extends CenterPopupView{

    private String textContent;
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


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        RelativeLayout rl = findViewById(R.id.input_commit);
        TextView tv = findViewById(R.id.tv_text);
        tv.setEnabled(false);

        VerificationCodeInput vr = findViewById(R.id.vci_invite);
        vr.focus();
        vr.setOnCompleteListener(content -> {
            textContent  = content;
            rl.setBackground(getResources().getDrawable(R.drawable.invite_click_1));
            tv.setEnabled(true);
        });

        tv.setOnClickListener(v->{
            mSelectListener.onSelect(2,textContent);
            postDelayed(this::dismiss,10);
        });

        ImageView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v->{
            postDelayed(this::dismiss,10);
        });
    }
}
