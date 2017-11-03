package com.zxcx.zhizhe.ui.my.collect.collectCard.adapter;

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
import com.zxcx.zhizhe.ui.my.collect.collectCard.CollectCardBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CollectCardAdapter extends BaseQuickAdapter<CollectCardBean,BaseViewHolder> {
    private boolean isDelete;
    private CollectCardCheckListener mListener;

    public interface CollectCardCheckListener {
        void onCheckedChanged(CollectCardBean bean, int position, boolean isChecked);
    }

    public CollectCardAdapter(@Nullable List<CollectCardBean> data, CollectCardCheckListener listener) {
        super(R.layout.item_collect_card, data);
        mListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CollectCardBean item) {

        helper.setText(R.id.tv_item_collect_card_title,item.getName());
        helper.setText(R.id.tv_item_collect_card_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));

        RelativeLayout relativeLayout = helper.getView(R.id.rl_item_collect_card);
        RelativeLayout rlTop = helper.getView(R.id.rl_item_collect_card_top);
        final CheckBox checkBox = helper.getView(R.id.cb_item_collect_card);
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

        ViewGroup.LayoutParams para = rlTop.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(13 * 2)) / 2 * 3/4;
        rlTop.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        RoundedImageView imageView = helper.getView(R.id.iv_item_collect_card_icon);
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}