package com.zxcx.zhizhe.ui.home.hot;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by anm on 2017/12/4.
 */

public class RecommendBean implements MultiItemEntity{

    public static final int TYPE_CARD = 1;
    public static final int TYPE_CARD_BAG = 2;

    private boolean isCardBag;
    private HotCardBean mCardBean;
    private List<HotCardBagBean> mCardBagBeanList;

    public RecommendBean(boolean isCardBag, HotCardBean cardBean) {
        this.isCardBag = isCardBag;
        mCardBean = cardBean;
    }

    public RecommendBean(boolean isCardBag, List<HotCardBagBean> cardBagBeanList) {
        this.isCardBag = isCardBag;
        mCardBagBeanList = cardBagBeanList;
    }

    public boolean isCardBag() {
        return isCardBag;
    }

    public void setCardBag(boolean cardBag) {
        isCardBag = cardBag;
    }

    public HotCardBean getCardBean() {
        return mCardBean;
    }

    public void setCardBean(HotCardBean cardBean) {
        mCardBean = cardBean;
    }

    public List<HotCardBagBean> getCardBagBeanList() {
        return mCardBagBeanList;
    }

    public void setCardBagBeanList(List<HotCardBagBean> cardBagBeanList) {
        mCardBagBeanList = cardBagBeanList;
    }

    @Override
    public int getItemType() {
        if (isCardBag){
            return TYPE_CARD_BAG;
        }else {
            return TYPE_CARD;
        }
    }
}
