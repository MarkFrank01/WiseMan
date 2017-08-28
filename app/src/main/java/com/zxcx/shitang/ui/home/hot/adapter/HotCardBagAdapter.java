package com.zxcx.shitang.ui.home.hot.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardBagAdapter extends BaseQuickAdapter<HotCardBagBean,BaseViewHolder> {
    public HotCardBagAdapter(@Nullable List<HotCardBagBean> data) {
        super(R.layout.item_hot_card_bag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCardBagBean item) {

    }
}
