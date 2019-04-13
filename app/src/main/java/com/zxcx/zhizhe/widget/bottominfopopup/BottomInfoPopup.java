package com.zxcx.zhizhe.widget.bottominfopopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/6
 * @Description :
 */
public class BottomInfoPopup extends BottomPopupView{

    String info;
    int checkPosition;
    private OnSelectListener selectListener;

    public BottomInfoPopup(@NonNull Context context) {
        super(context);
    }

    public BottomInfoPopup(@NonNull Context context, String info, int checkPosition, OnSelectListener selectListener) {
        super(context);
        this.info = info;
        this.checkPosition = checkPosition;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bottom_info_bottom;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView info_tv = findViewById(R.id.info_content);
        info_tv.setText(info);

        TextView t1 = findViewById(R.id.info_cancel);
        t1.setOnClickListener(v->{
            postDelayed(this::dismiss,10);
        });

        TextView t2 = findViewById(R.id.info_queren);
        t2.setOnClickListener(v->{
            selectListener.onSelect(2,"完成");
            postDelayed(this::dismiss,10);
        });
    }
}
