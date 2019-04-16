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
 * @Created on 2019/4/8
 * @Description :
 */
public class CircleJoinXFPopup extends BottomPopupView{

    //数据内容
    String desc_content1;
    String desc_content2;
    String desc_content3;
    String desc_content4;

    String bottom_text;

    int checkPosition;

    private OnSelectListener selectListener;

    public CircleJoinXFPopup(@NonNull Context context) {
        super(context);
    }


    public CircleJoinXFPopup(@NonNull Context context, String desc_content1, String desc_content2, String desc_content3, String desc_content4, int checkPosition, OnSelectListener selectListener) {
        super(context);
        this.desc_content1 = desc_content1;
        this.desc_content2 = desc_content2;
        this.desc_content3 = desc_content3;
        this.desc_content4 = desc_content4;
        this.checkPosition = checkPosition;
        this.selectListener = selectListener;
    }

    public CircleJoinXFPopup(@NonNull Context context, String desc_content1, String desc_content2, String desc_content3, String desc_content4, String bottom_text, int checkPosition, OnSelectListener selectListener) {
        super(context);
        this.desc_content1 = desc_content1;
        this.desc_content2 = desc_content2;
        this.desc_content3 = desc_content3;
        this.desc_content4 = desc_content4;
        this.bottom_text = bottom_text;
        this.checkPosition = checkPosition;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_join_2;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView info_tv = findViewById(R.id.bottom_tv1);
        info_tv.setText(desc_content1);

        TextView info_tv_2 = findViewById(R.id.bottom_tv2);
        info_tv_2.setText(desc_content2);

        TextView info_tv_3 = findViewById(R.id.bottom_tv_3);
        info_tv_3.setText(desc_content3);

        TextView info_tv_4 = findViewById(R.id.bottom_tv_4);
        float a = Float.parseFloat(desc_content4);
        if (a>0){
            info_tv_4.setTextColor(getResources().getColor(R.color.button_blue));
            info_tv_4.setText(desc_content4+"智者币");

            Button bt = findViewById(R.id.bottom_bt);
            bt.setText(bottom_text);
            bt.setOnClickListener(v -> {
                selectListener.onSelect(2,"完成");
                postDelayed(this::dismiss,10);
            });
        }else {
            info_tv_4.setTextColor(getResources().getColor(R.color.red));
            info_tv_4.setText(desc_content4+"智者币");

            Button bt = findViewById(R.id.bottom_bt);
            bt.setText(bottom_text);
            bt.setOnClickListener(v -> {
                selectListener.onSelect(3,"完成");
                postDelayed(this::dismiss,10);
            });
        }


    }
}
