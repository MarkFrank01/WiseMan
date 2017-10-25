package com.zxcx.zhizhe.ui.classify;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/8/30.
 */

public class ClassifyCardBagBean extends RetrofitBaen implements MultiItemEntity{

    public static final int TYPE_CARD_BAG = 2;

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;

    @Override
    public int getItemType() {
        return TYPE_CARD_BAG;
    }

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
