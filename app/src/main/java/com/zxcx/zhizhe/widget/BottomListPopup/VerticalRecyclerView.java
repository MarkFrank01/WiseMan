package com.zxcx.zhizhe.widget.BottomListPopup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.zxcx.zhizhe.R;

/**
 * @author : MarkFrank01
 * @Created on 2019/2/21
 * @Description :
 */
public class VerticalRecyclerView extends RecyclerView{

    public VerticalRecyclerView(Context context) {
        this(context,null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        addItemDecoration(decoration);
    }
}
