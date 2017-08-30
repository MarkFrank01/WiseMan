package com.zxcx.shitang.ui.classify;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by anm on 2017/8/30.
 */

public class ClassifyCardBagBean implements MultiItemEntity{

    public static final int TYPE_CARD_BAG = 2;

    private int id;
    private String imageUrl;
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
