package com.zxcx.zhizhe.widget.bottomdescpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/4
 * @Description :
 */
public class CircleBottomWTGPopup extends BottomPopupView {


    //数据内容
    String desc_content1;
    String desc_content2;
    int checkPosition;


    private OnSelectListener selectListener;

    //底部按钮提示

    public CircleBottomWTGPopup(@NonNull Context context) {
        super(context);
    }

    public CircleBottomWTGPopup(@NonNull Context context, String desc_content1, String desc_content2, int checkPosition, OnSelectListener selectListener) {
        super(context);
        this.desc_content1 = desc_content1;
        this.desc_content2 = desc_content2;
        this.checkPosition = checkPosition;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_bottom_desc_wtg;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView info_tv = findViewById(R.id.bottom_tv1);
        info_tv.setText(desc_content1);

        TextView info_tv_2 = findViewById(R.id.bottom_tv2);
        info_tv_2.setText(desc_content2);

        Button bt = findViewById(R.id.bottom_bt);
        bt.setOnClickListener(v -> {
            postDelayed(this::dismiss,10);
        });
    }

}
