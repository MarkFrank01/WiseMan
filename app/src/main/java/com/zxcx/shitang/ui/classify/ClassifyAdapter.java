package com.zxcx.shitang.ui.classify;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;

import java.util.List;

/**
 * Created by anm on 2017/5/23.
 */

public class ClassifyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {


    public ClassifyAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_classify_classify);
        addItemType(ClassifyBean.TYPE_CARD_BAG,R.layout.item_classify);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ClassifyBean.TYPE_CLASSIFY:
                if (((ClassifyBean)item).getImgUrl() == null){
                    helper.setVisible(R.id.rl_item_classify_ad,false);
                }else {
                    helper.setVisible(R.id.rl_item_classify_ad,true);
                }
                if (helper.getLayoutPosition() == 0){
                    helper.setVisible(R.id.bg_item_classify,false);
                }
        }
    }
}
