package com.zxcx.shitang.ui.home.hot;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

public class HotCardBagBean extends RetrofitBaen {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

