package com.zxcx.shitang.ui.classify;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;
import com.zxcx.shitang.utils.ScreenUtils;

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
                    helper.addOnClickListener(R.id.iv_item_classify_ad);
                }
                if (helper.getLayoutPosition() == 0){
                    helper.setVisible(R.id.bg_item_classify,false);
                }
                break;
            case ClassifyBean.TYPE_CARD_BAG:

                helper.addOnClickListener(R.id.rl_item_classify);

                ViewGroup.LayoutParams para = helper.itemView.getLayoutParams();
                int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
                para.width = (screenWidth - ScreenUtils.dip2px(12*2) - ScreenUtils.dip2px(10*2)) / 3;
                para.height = (screenWidth - ScreenUtils.dip2px(12*2) - ScreenUtils.dip2px(10*2)) / 3;
                helper.itemView.setLayoutParams(para);
                break;
        }
    }
}
