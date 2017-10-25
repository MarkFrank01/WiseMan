package com.zxcx.zhizhe.ui.my.collect.collectFolder.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CollectFolderAdapter extends BaseQuickAdapter<CollectFolderBean,BaseViewHolder> {

    private boolean isDelete;
    private CollectFolderCheckListener mListener;

    public interface CollectFolderCheckListener{
        void onCheckedChanged(CollectFolderBean bean, int position, boolean isChecked);
    }

    public CollectFolderAdapter(@Nullable List<CollectFolderBean> data, CollectFolderCheckListener listener) {
        super(R.layout.item_collect_folder, data);
        mListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CollectFolderBean item) {

        helper.setText(R.id.tv_item_collect_folder_title,item.getName());
        helper.setText(R.id.tv_item_collect_folder_time, DateTimeUtils.getDate(item.getTime()));
        helper.setText(R.id.tv_item_collect_folder_num,item.getNum()+"");

        RelativeLayout relativeLayout = helper.getView(R.id.rl_item_collect_folder);
        final CheckBox checkBox = helper.getView(R.id.cb_item_collect_folder);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        if (isDelete){
            relativeLayout.setVisibility(View.VISIBLE);
            if (item.isChecked()){
                checkBox.setChecked(true);
            }else {
                checkBox.setChecked(false);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setChecked(isChecked);
                    mListener.onCheckedChanged(item,helper.getAdapterPosition(),isChecked);
                }
            });
        }else {
            relativeLayout.setVisibility(View.GONE);
            item.setChecked(false);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(false);
        }

        RoundedImageView imageView = helper.getView(R.id.iv_item_collect_folder_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(12 * 2) - ScreenUtils.dip2px(15)) / 2 * 3/4;
        imageView.setLayoutParams(para);
        relativeLayout.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
