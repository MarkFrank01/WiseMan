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
public class CircleBottomPopup extends BottomPopupView {

    String name;
    String time;
    String type;
    String reason;
    private OnSelectListener selectListener;

    public CircleBottomPopup(@NonNull Context context) {
        super(context);
    }

    public CircleBottomPopup(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
    }

    public CircleBottomPopup(@NonNull Context context, String name, String time, String type, String reason, OnSelectListener selectListener) {
        super(context);
        this.name = name;
        this.time = time;
        this.type = type;
        this.reason = reason;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_bottom_desc;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        Button bt = findViewById(R.id.bottom_bt);
        bt.setOnClickListener(v -> {
            postDelayed(this::dismiss,100);
        });
    }

}
