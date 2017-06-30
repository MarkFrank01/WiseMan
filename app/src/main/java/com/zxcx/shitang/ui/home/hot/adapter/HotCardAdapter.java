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

public class HotCardAdapter extends BaseQuickAdapter<HotBean,BaseViewHolder> {
    public HotCardAdapter(@Nullable List<HotBean> data) {
        super(R.layout.item_home_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotBean item) {

    }
}
