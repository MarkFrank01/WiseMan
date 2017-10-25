package com.zxcx.zhizhe.ui.search.result.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.search.result.SearchCardBagBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SearchResultCardBagAdapter extends BaseQuickAdapter<SearchCardBagBean,BaseViewHolder> {

    private String mKeyword;

    public SearchResultCardBagAdapter(@Nullable List<SearchCardBagBean> data, String keyword) {
        super(R.layout.item_attention_card_bag, data);
        mKeyword = keyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchCardBagBean item) {
        TextView title = helper.getView(R.id.tv_item_home_card_bag_title);
        StringUtils.setTextviewColorAndBold(title,mKeyword,item.getName());
        RoundedImageView imageView = helper.getView(R.id.iv_item_home_card_bag_icon);
        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);
    }
}
