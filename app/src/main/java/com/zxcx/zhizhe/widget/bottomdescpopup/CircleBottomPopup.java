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
public class CircleBottomPopup extends BottomPopupView {


    //数据内容
    String desc_content1;
    int checkPosition;


    private OnSelectListener selectListener;

    //底部按钮提示

    public CircleBottomPopup(@NonNull Context context) {
        super(context);
    }

    public CircleBottomPopup(@NonNull Context context, String desc_content1, int checkPosition, OnSelectListener selectListener) {
        super(context);
        this.desc_content1 = desc_content1;
        this.checkPosition = checkPosition;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_bottom_desc;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView info_tv = findViewById(R.id.bottom_tv1);
        info_tv.setText(desc_content1);

        Button bt = findViewById(R.id.bottom_bt);
        bt.setOnClickListener(v -> {
            postDelayed(this::dismiss,10);
        });
    }

}
