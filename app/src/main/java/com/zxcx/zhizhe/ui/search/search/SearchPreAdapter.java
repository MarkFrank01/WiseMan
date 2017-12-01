package com.zxcx.zhizhe.ui.search.search;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.TextViewUtils;

import java.util.List;

/**
 * Created by anm on 2017/11/30.
 */

public class SearchPreAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    private String mKeyword;

    public SearchPreAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_pre,data);
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tv_item_search_pre);
        TextViewUtils.setTextViewColorAndBold(textView,mKeyword,item);
    }
}
