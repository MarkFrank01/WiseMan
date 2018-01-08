package com.zxcx.zhizhe.ui.home.hot;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.List;

public class HotCardBagBean extends RetrofitBaen {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String name;
    @SerializedName("articleList")
    private List<HotCardBean> cardList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HotCardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<HotCardBean> cardList) {
        this.cardList = cardList;
    }
}

