package com.zxcx.zhizhe.ui.classify;

import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/5/23.
 */

public class ClassifyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {


    ClassifyAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_classify_classify);
        addItemType(ClassifyCardBagBean.TYPE_CARD_BAG,R.layout.item_classify);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ClassifyBean.TYPE_CLASSIFY:
                ClassifyBean bean = (ClassifyBean) item;
                TextView title = helper.getView(R.id.tv_item_classify_classify);
                title.setText(bean.getTitle());
                TextPaint paint = title.getPaint();
                paint.setFakeBoldText(true);
                break;
            case ClassifyCardBagBean.TYPE_CARD_BAG:

                helper.addOnClickListener(R.id.rl_item_classify);

                ViewGroup.LayoutParams para = helper.itemView.getLayoutParams();
                int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
                para.width = (screenWidth - ScreenUtils.dip2px(15*2) - ScreenUtils.dip2px(20*2)) / 3;
                helper.itemView.setLayoutParams(para);

                ClassifyCardBagBean cardBagBean = (ClassifyCardBagBean) item;
                helper.setText(R.id.tv_item_card_bag_name,cardBagBean.getName());
                break;
        }
    }
}
