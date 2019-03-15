package com.zxcx.zhizhe.widget.BottomListPopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
public class HuatiManagePopup extends BottomPopupView{

    int type;
    boolean isFix;
    int checkedPosition = -1;
    private OnSelectListener selectListener;

    public HuatiManagePopup(@NonNull Context context, int type, boolean isFix, int checkedPosition, OnSelectListener selectListener) {
        super(context);
        this.type = type;
        this.isFix = isFix;
        this.checkedPosition = checkedPosition;
        this.selectListener = selectListener;
    }

    public HuatiManagePopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_huati_bottom;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView tv_1 = findViewById(R.id.tv_text);

        if (type == 1){
            tv_1.setVisibility(GONE);
        }

        if (type == 2){
            tv_1.setText("取消置顶");
        }

        if (type == 3){
            tv_1.setText("置顶话题");
        }

        tv_1.setOnClickListener(v->{
            if (type == 2){
                selectListener.onSelect(2,"2");
            }else if (type == 3){
                selectListener.onSelect(0, "0");
            }else if (type == 1){
                selectListener.onSelect(1, "1");

            }
            postDelayed(this::dismiss,10);
        });

        TextView tv_2 = findViewById(R.id.tv_text_2);
        tv_2.setOnClickListener(v->{
            selectListener.onSelect(1,"1");
            postDelayed(this::dismiss,10);
        });

    }
}
