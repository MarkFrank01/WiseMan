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

public class SearchResultCardAdapter extends BaseQuickAdapter<SearchResultBean,BaseViewHolder> {
    public SearchResultCardAdapter(@Nullable List<SearchResultBean> data) {
        super(R.layout.item_card_bag_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResultBean item) {
        TextView title = helper.getView(R.id.tv_item_card_bag_list_title);
        StringUtils.setTextviewColorAndBold(title,"笔","联想笔记本");
    }
}
