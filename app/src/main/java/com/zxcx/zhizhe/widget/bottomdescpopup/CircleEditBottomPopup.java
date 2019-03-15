package com.zxcx.zhizhe.widget.bottomdescpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/4
 * @Description :
 */
public class CircleEditBottomPopup extends BottomPopupView {

    private OnSelectListener selectListener;

    public CircleEditBottomPopup(@NonNull Context context) {
        super(context);
    }

    public CircleEditBottomPopup(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_edit_popup_bottom;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        Button bt = findViewById(R.id.bottom_bt);
        bt.setOnClickListener(v -> {
            postDelayed(this::dismiss,10);
        });
    }

}
