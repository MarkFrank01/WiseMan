package com.zxcx.shitang.ui.card.card.collect;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class SelectCollectFolderAdapter extends BaseQuickAdapter<CollectFolderBean,BaseViewHolder> {
    public SelectCollectFolderAdapter(@Nullable List<CollectFolderBean> data) {
        super(R.layout.item_dialog_collect_folder, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectFolderBean item) {

    }
}
