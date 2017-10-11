package com.zxcx.zhizhe.ui.search.result.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.search.result.SearchCardBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SearchResultCardAdapter extends BaseQuickAdapter<SearchCardBean,BaseViewHolder> {

    private String mKeyword;

    public SearchResultCardAdapter(@Nullable List<SearchCardBean> data, String keyword) {
        super(R.layout.item_card_bag_list, data);
        mKeyword = keyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchCardBean item) {
        TextView title = helper.getView(R.id.tv_item_card_bag_list_title);
        StringUtils.setTextviewColorAndBold(title,mKeyword,item.getName());
        RoundedImageView imageView = helper.getView(R.id.iv_item_card_bag_list_icon);
        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.mipmap.image_morenlogo,imageView);
        helper.setText(R.id.tv_item_card_bag_list_collect_num,mContext.getString(R.string.tv_item_home_card_num,item.getCollectNum(),item.getLikeNum()));
    }
}
