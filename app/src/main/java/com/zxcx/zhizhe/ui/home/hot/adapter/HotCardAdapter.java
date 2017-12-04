package com.zxcx.zhizhe.ui.home.hot.adapter;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.home.hot.HotCardBagBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.ui.home.hot.RecommendBean;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.WrapContentHeightViewPager;

import java.util.List;

/**
 * Created by anm on 2017/6/26.
 */

public class HotCardAdapter extends BaseMultiItemQuickAdapter<RecommendBean,BaseViewHolder> {

    private HotCardBagCardOnClickListener mListener;

    public interface HotCardBagCardOnClickListener{
        void hotCardBagCardOnClick(HotCardBean bean);
        void hotCardBagOnClick(HotCardBagBean bean);
    }

    public HotCardAdapter(@Nullable List<RecommendBean> data, HotCardBagCardOnClickListener listener) {
        super(data);
        addItemType(RecommendBean.TYPE_CARD, R.layout.item_home_card);
        addItemType(RecommendBean.TYPE_CARD_BAG,R.layout.item_hot_card_bag);
        mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendBean item) {
        switch (helper.getItemViewType()) {
            case RecommendBean.TYPE_CARD:
                initCardView(helper, item);
                break;
            case RecommendBean.TYPE_CARD_BAG:
                initCardBagView(helper, item);
                break;
        }

    }

    private void initCardView(BaseViewHolder helper, RecommendBean bean) {
        HotCardBean item = bean.getCardBean();
        ImageView imageView = helper.getView(R.id.iv_item_home_card_icon);
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth - ScreenUtils.dip2px(20 * 2)) * 9/16;
        imageView.setLayoutParams(para);

        String imageUrl = ZhiZheUtils.getHDImageUrl(item.getImageUrl());
        ImageLoader.load(mContext,imageUrl,R.drawable.default_card,imageView);

        helper.setText(R.id.tv_item_home_card_title,item.getName());
        helper.setText(R.id.tv_item_home_card_info, mContext.getString(R.string.tv_card_info, item.getDate(), item.getAuthor()));

        TextView title = helper.getView(R.id.tv_item_home_card_title);
        TextPaint paint = title.getPaint();
        paint.setFakeBoldText(true);
    }

    private void initCardBagView(BaseViewHolder helper, RecommendBean bean) {
        List<HotCardBagBean> cardBagBeanList = bean.getCardBagBeanList();
        WrapContentHeightViewPager viewPager = helper.getView(R.id.vp_item_hot_card_bag);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(ScreenUtils.dip2px(20), 0, ScreenUtils.dip2px(20), 0);
        viewPager.setPageMargin(ScreenUtils.dip2px(20));
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return cardBagBeanList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                HotCardBagBean bagBean = cardBagBeanList.get(position);
                View cardBagView = View.inflate(mContext,R.layout.item_item_hot_card_bag,null);
                TextView textView = (TextView) cardBagView.findViewById(R.id.tv_item_home_card_bag);
                textView.setText(bagBean.getName());
                RecyclerView recyclerView = (RecyclerView) cardBagView.findViewById(R.id.rv_item_home_card_bag);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                HotCardBagAdapter adapter = new HotCardBagAdapter(bagBean.getCardList());
                //设置卡包内的卡片的点击动作
                adapter.setOnItemClickListener((adapter1, view, position1) -> {
                    mListener.hotCardBagCardOnClick(bagBean.getCardList().get(position1));
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                //设置卡包的点击动作
                cardBagView.setOnClickListener(v -> {
                    mListener.hotCardBagOnClick(bagBean);
                });
                container.addView(cardBagView);
                return cardBagView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }
}
