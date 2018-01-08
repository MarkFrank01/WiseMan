package com.zxcx.zhizhe.ui.home.hot;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.List;

/**
 * Created by anm on 2017/12/4.
 */

public class HotBean extends RetrofitBaen {

    @SerializedName("articleList")
    private List<HotCardBean> cardList;
    @SerializedName("collectionList")
    private List<HotCardBagBean> cardBagList;

    public List<HotCardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<HotCardBean> cardList) {
        this.cardList = cardList;
    }

    public List<HotCardBagBean> getCardBagList() {
        return cardBagList;
    }

    public void setCardBagList(List<HotCardBagBean> cardBagList) {
        this.cardBagList = cardBagList;
    }
}
