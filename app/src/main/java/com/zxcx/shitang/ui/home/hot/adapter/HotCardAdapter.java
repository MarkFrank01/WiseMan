package com.zxcx.shitang.ui.home.hot.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.home.hot.HotCardBean;
import com.zxcx.shitang.utils.ImageLoader;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardAdapter extends BaseQuickAdapter<HotCardBean,BaseViewHolder> {

    private Context mContext;

    public HotCardAdapter(@Nullable List<HotCardBean> data, Context context) {
        super(R.layout.item_home_card, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBean item) {
        RoundedImageView imageView = helper.getView(R.id.iv_item_home_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(12 * 2) - ScreenUtils.dip2px(15)) / 2 * 3/4;
        imageView.setLayoutParams(para);

        helper.addOnClickListener(R.id.tv_item_home_card_type);
        ImageLoader.load(mContext,item.getImageUrl(),R.mipmap.image_morenlogo,imageView);
        helper.setText(R.id.tv_item_home_card_title,item.getName());
        helper.setText(R.id.tv_item_home_card_type,item.getBagName());
        helper.setText(R.id.tv_item_home_card_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));
    }
}
