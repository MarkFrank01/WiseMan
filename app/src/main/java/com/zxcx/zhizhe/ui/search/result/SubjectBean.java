package com.zxcx.zhizhe.ui.search.result;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

public class SubjectBean extends RetrofitBaen {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String name;
    @SerializedName("articleList")
    private List<CardBean> cardList;

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

    public List<CardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardBean> cardList) {
        this.cardList = cardList;
    }
}

