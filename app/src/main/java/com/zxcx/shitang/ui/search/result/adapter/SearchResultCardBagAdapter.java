package com.zxcx.shitang.ui.search.result.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.search.result.SearchResultBean;
import com.zxcx.shitang.utils.StringUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SearchResultCardBagAdapter extends BaseQuickAdapter<SearchResultBean,BaseViewHolder> {
    public SearchResultCardBagAdapter(@Nullable List<SearchResultBean> data) {
        super(R.layout.item_hot_card_bag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultBean item) {
        TextView title = helper.getView(R.id.tv_item_home_card_bag_title);
        StringUtils.setTextviewColorAndBold(title,"笔","钢笔");
    }
}
