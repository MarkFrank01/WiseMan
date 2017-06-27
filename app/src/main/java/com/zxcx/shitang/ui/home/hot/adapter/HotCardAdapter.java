package com.zxcx.shitang.ui.home.hot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.ui.home.hot.HotBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardAdapter extends BaseQuickAdapter<HotBean,BaseViewHolder> {
    public HotCardAdapter(@LayoutRes int layoutResId, @Nullable List<HotBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotBean item) {

    }
}
