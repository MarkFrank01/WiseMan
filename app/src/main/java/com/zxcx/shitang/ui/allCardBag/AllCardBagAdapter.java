package com.zxcx.shitang.ui.allCardBag;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;

import java.util.List;

/**
 * Created by anm on 2017/5/23.
 */

public class AllCardBagAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {


    public AllCardBagAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(0, R.layout.item_title_all_card_bag);
        addItemType(1,R.layout.item_attention);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        if (helper.getItemViewType() == 0){
            setFullSpan(helper);
            helper.setText(R.id.tv_item_title_all_card_bag,"666");
            
            TextView tv = helper.getView(R.id.tv_item_title_all_card_bag);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = helper.getAdapterPosition();
                    expand(pos);
                }
            });
        }else {
            int pos = helper.getAdapterPosition();
            helper.setText(R.id.tv_item_select_attention,((AllCardBagBean.ContentBean)mData.get(pos)).getTitle());
        }
    }
}
