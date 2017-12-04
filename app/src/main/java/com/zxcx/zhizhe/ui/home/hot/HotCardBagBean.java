package com.zxcx.zhizhe.ui.home.hot;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.List;

public class HotCardBagBean extends RetrofitBaen {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "articleList")
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

