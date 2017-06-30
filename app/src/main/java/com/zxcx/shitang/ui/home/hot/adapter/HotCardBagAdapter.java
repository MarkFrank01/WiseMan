package com.zxcx.shitang.ui.home.hot.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.home.hot.HotBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardBagAdapter extends BaseQuickAdapter<HotBean,BaseViewHolder> {
    public HotCardBagAdapter(@Nullable List<HotBean> data) {
        super(R.layout.item_home_card_bag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotBean item) {

    }
}
