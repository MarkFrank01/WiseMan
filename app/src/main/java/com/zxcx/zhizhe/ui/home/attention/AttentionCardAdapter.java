package com.zxcx.zhizhe.ui.home.attention;

import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class AttentionCardAdapter extends BaseQuickAdapter<HotCardBean,BaseViewHolder> {

    public AttentionCardAdapter(@Nullable List<HotCardBean> data) {
        super(R.layout.item_home_card,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBean item) {
        ImageView imageView = helper.getView(R.id.iv_item_home_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(20 * 2)) * 9/16;
        imageView.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);

        helper.setText(R.id.tv_item_home_card_title,item.getName());
        helper.setText(R.id.tv_item_home_card_info, mContext.getString(R.string.tv_card_info, DateTimeUtils.getDateString(item.getDate()), item.getAuthor()));

        TextView title = helper.getView(R.id.tv_item_home_card_title);
        TextPaint paint = title.getPaint();
        paint.setFakeBoldText(true);

    }
}
