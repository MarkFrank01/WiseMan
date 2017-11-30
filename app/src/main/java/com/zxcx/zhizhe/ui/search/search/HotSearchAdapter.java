package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;

import java.util.List;

/**
 * Created by anm on 2017/11/30.
 */

public class HotSearchAdapter extends BaseQuickAdapter<SearchBean,BaseViewHolder> {

    public HotSearchAdapter(@Nullable List<SearchBean> data) {
        super(R.layout.item_search_history,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean item) {
        helper.setText(R.id.tv_item_search_history,item.getConent());
    }
}
