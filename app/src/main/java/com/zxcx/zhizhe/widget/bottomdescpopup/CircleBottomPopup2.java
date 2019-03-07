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
public class CircleBottomPopup2 extends BottomPopupView {

    String circlename;
    String circleprice;
    String circleendtime;
    String circleyue;


    private OnSelectListener selectListener;

    public CircleBottomPopup2(@NonNull Context context) {
        super(context);
    }

    public CircleBottomPopup2(@NonNull Context context, OnSelectListener selectListener) {
        super(context);
        this.selectListener = selectListener;
    }

    public CircleBottomPopup2(@NonNull Context context, String circlename, String circleprice, String circleendtime, String circleyue, OnSelectListener selectListener) {
        super(context);
        this.circlename = circlename;
        this.circleprice = circleprice;
        this.circleendtime = circleendtime;
        this.circleyue = circleyue;
        this.selectListener = selectListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_bottom_desc_2;
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
