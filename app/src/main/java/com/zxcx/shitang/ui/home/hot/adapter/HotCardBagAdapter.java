package com.zxcx.shitang.ui.home.hot.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.utils.ImageLoader;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardBagAdapter extends BaseQuickAdapter<HotCardBagBean,BaseViewHolder> {

    private Context mContext;

    public HotCardBagAdapter(@Nullable List<HotCardBagBean> data, Context context) {
        super(R.layout.item_hot_card_bag, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBagBean item) {
        helper.setText(R.id.tv_item_home_card_bag_title,item.getName());
        ImageView imageView = helper.getView(R.id.iv_item_home_card_bag_icon);
        ImageLoader.load(mContext,item.getImageUrl(),R.mipmap.image_morenlogo,imageView);
    }
}
