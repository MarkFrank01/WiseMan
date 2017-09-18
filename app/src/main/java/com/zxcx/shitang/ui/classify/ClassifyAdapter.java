package com.zxcx.shitang.ui.classify;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;
import com.zxcx.shitang.utils.ImageLoader;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.List;

/**
 * Created by anm on 2017/5/23.
 */

public class ClassifyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {


    public ClassifyAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_classify_classify);
        addItemType(ClassifyCardBagBean.TYPE_CARD_BAG,R.layout.item_classify);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ClassifyBean.TYPE_CLASSIFY:
                ClassifyBean bean = (ClassifyBean) item;
                String title = bean.getTitle();
                helper.setText(R.id.tv_item_classify_classify,title);
                ImageView imageView = helper.getView(R.id.iv_item_classify_classify);
                ImageLoader.load(mContext,bean.getImageUrl(),R.drawable.iv_item_classify_classify_icon_placeholder,imageView);
                break;
            case ClassifyCardBagBean.TYPE_CARD_BAG:

                helper.addOnClickListener(R.id.rl_item_classify);

                ViewGroup.LayoutParams para = helper.itemView.getLayoutParams();
                int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
                para.width = (screenWidth - ScreenUtils.dip2px(5*3) - ScreenUtils.dip2px(12*2)) / 4;
                helper.itemView.setLayoutParams(para);

                ClassifyCardBagBean cardBagBean = (ClassifyCardBagBean) item;
                helper.setText(R.id.tv_item_card_bag_name,cardBagBean.getName());
                break;
        }
    }
}
