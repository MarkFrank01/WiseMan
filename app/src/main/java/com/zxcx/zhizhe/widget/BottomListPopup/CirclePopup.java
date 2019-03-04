package com.zxcx.zhizhe.widget.BottomListPopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zxcx.zhizhe.R;

import java.util.Arrays;

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
public class CirclePopup extends BottomPopupView {

    RecyclerView recyclerView;

    public CirclePopup(@NonNull Context context) {
        super(context);
    }

    public CirclePopup(@NonNull Context context, String title, String[] data, int[] iconIds, int checkedPosition, OnSelectListener selectListener) {
        super(context);
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        this.selectListener = selectListener;
        this.checkedPosition = checkedPosition;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.circle_popup_bottom;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.recyclerView1);

        TextView tv = findViewById(R.id.tv_title1);
        tv.setText(title);

        final CommonAdapter<String> adapter = new CommonAdapter<String>(R.layout.adapter_text, Arrays.asList(data)) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.tv_text, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.setVisible(R.id.iv_image, true);
                    holder.setBackgroundRes(R.id.iv_image, iconIds[position]);
                } else {
                    holder.setVisible(R.id.iv_image, false);
                }

                if (checkedPosition != -1) {
//                    holder.setVisible(R.id.check_view,position == checkedPosition);
//                    holder.<CheckView>getView(R.id.check_view).setColor(getResources().getColor(R.color.black));
                    holder.setTextColor(R.id.tv_text, position == checkedPosition ?
                            getResources().getColor(R.color.button_blue) : getResources().getColor(R.color.toolbar_title));
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getDatas().get(position));
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                postDelayed(() -> {
                    dismiss();
                }, 100);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    String title;
    String[] data;
    int[] iconIds;

    public CirclePopup setStringData(String title, String[] data, int[] iconIds) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public CirclePopup setOnSelecListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    //设置默认选中的位置
    public CirclePopup setCheckedPostion(int postion) {
        this.checkedPosition = postion;
        return this;
    }
}
