package com.zxcx.zhizhe.widget.bottomdescpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.lxj.xpopup.core.BottomPopupView;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/4
 * @Description :
 */
public class CircleBottomPopup extends BottomPopupView {

    //标题
    String desc_title1;
    String desc_title2;
    String desc_title3;

    //数据内容
    String desc_content1;
    String desc_content2;
    String desc_content3;

    //底部按钮提示
    String desc_bottom;

    public CircleBottomPopup(@NonNull Context context) {
        super(context);
    }

    public CircleBottomPopup(@NonNull Context context, String desc_title1, String desc_title2, String desc_content1, String desc_content2,  String desc_bottom) {
        super(context);
        this.desc_title1 = desc_title1;
        this.desc_title2 = desc_title2;
        this.desc_content1 = desc_content1;
        this.desc_content2 = desc_content2;
        this.desc_bottom = desc_bottom;
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
