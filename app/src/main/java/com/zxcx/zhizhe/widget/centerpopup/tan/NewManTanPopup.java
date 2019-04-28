package com.zxcx.zhizhe.widget.centerpopup.tan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/4/28
 * @Description :
 */
public class NewManTanPopup extends CenterPopupView{

    private String textContent;
    private OnSelectListener mSelectListener;

    public NewManTanPopup(@NonNull Context context) {
        super(context);
    }

    public NewManTanPopup(@NonNull Context context, String textContent, OnSelectListener selectListener) {
        super(context);
        this.textContent = textContent;
        mSelectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.tan_new_man;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        RelativeLayout rl = findViewById(R.id.input_commit);

        rl.setOnClickListener(v->{
            mSelectListener.onSelect(2,"666");
            postDelayed(this::dismiss,10);
        });

        ImageView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v->{
            postDelayed(this::dismiss,10);
        });
    }
}
