package com.zxcx.zhizhe.ui.home.hot.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardAdapter extends BaseQuickAdapter<HotCardBean,BaseViewHolder> {

    public HotCardAdapter(@Nullable List<HotCardBean> data) {
        super(R.layout.item_home_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBean item) {
        RoundedImageView imageView = helper.getView(R.id.iv_item_home_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(12 * 2) - ScreenUtils.dip2px(15)) / 2 * 3/4;
        imageView.setLayoutParams(para);

        helper.addOnClickListener(R.id.fl_item_home_card_type);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.mipmap.image_morenlogo,imageView);

        helper.setText(R.id.tv_item_home_card_title,item.getName());
        helper.setText(R.id.tv_item_home_card_type,item.getBagName());
        helper.setText(R.id.tv_item_home_card_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));

        TextView title = helper.getView(R.id.tv_item_home_card_title);
        TextPaint paint = title.getPaint();
        paint.setFakeBoldText(true);

        if (item.getColor() != null) {
            TextView cardBag = helper.getView(R.id.tv_item_home_card_type);
            cardBag.setTextColor(Color.parseColor(item.getColor()));
            GradientDrawable drawable =(GradientDrawable)cardBag.getBackground();
            drawable.setStroke(1,Color.parseColor(item.getColor()));
        }
    }
}
