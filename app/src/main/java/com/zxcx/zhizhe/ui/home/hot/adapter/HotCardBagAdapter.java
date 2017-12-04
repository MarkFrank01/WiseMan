package com.zxcx.zhizhe.ui.home.hot.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardBagAdapter extends BaseQuickAdapter<HotCardBean,BaseViewHolder> {

    public HotCardBagAdapter(@Nullable List<HotCardBean> data) {
        super(R.layout.item_search_result_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBean item) {
        helper.setText(R.id.tv_item_search_result_card_name,item.getName());
        ImageView imageView = helper.getView(R.id.iv_item_search_result_card);
        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);
        helper.setText(R.id.tv_item_search_result_card_info, mContext.getString(R.string.tv_card_info, item.getDate(), item.getAuthor()));
        helper.setVisible(R.id.view_line,helper.getAdapterPosition() != getItemCount()-1);
    }
}
