package com.zxcx.zhizhe.widget.bottomsharepopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/9
 * @Description :
 */
public class CircleBottomSharePopup extends BottomPopupView {

    private OnSelectListener selectListener;

    public CircleBottomSharePopup(@NonNull Context context) {
        super(context);
    }

    public CircleBottomSharePopup(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_share_bottom;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ImageView iv1 = findViewById(R.id.share1);
        iv1.setOnClickListener(v -> {
            selectListener.onSelect(1, "1");
            postDelayed(this::dismiss,100);
        });

        ImageView iv2 = findViewById(R.id.share2);
        iv2.setOnClickListener(v -> {
            selectListener.onSelect(2, "2");
            postDelayed(this::dismiss,100);
        });

        ImageView iv3 = findViewById(R.id.share3);
        iv3.setOnClickListener(v->{
            selectListener.onSelect(3,"3");
            postDelayed(this::dismiss,100);
        });

        ImageView iv4 = findViewById(R.id.share4);
        iv4.setOnClickListener(v->{
            selectListener.onSelect(4,"4");
            postDelayed(this::dismiss,100);
        });
    }
}
