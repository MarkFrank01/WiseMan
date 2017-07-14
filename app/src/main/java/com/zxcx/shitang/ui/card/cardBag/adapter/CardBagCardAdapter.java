package com.zxcx.shitang.ui.card.cardBag.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class CardBagCardAdapter extends BaseQuickAdapter<CollectFolderBean,BaseViewHolder> {
    public CardBagCardAdapter( @Nullable List<CollectFolderBean> data) {
        super(R.layout.item_card_bag_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectFolderBean item) {


        RoundedImageView imageView = helper.getView(R.id.iv_item_card_bag_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(12 * 2) - ScreenUtils.dip2px(15)) / 2 * 3/4;
        imageView.setLayoutParams(para);
    }
}
