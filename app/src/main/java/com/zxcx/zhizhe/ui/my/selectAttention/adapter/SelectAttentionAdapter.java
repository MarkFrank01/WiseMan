package com.zxcx.zhizhe.ui.my.selectAttention.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SelectAttentionAdapter extends BaseQuickAdapter<SelectAttentionBean,BaseViewHolder> {

    public SelectAttentionAdapter(@Nullable List<SelectAttentionBean> data) {
        super(R.layout.item_select_attention, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SelectAttentionBean item) {
        if (item.isChecked()){
            helper.setChecked(R.id.cb_item_select_attention_name,true);
        }else {
            helper.setChecked(R.id.cb_item_select_attention_name,false);
        }

        helper.setText(R.id.cb_item_select_attention_name,item.getName());
    }
}
