package com.zxcx.shitang.ui.classify;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ClassifyBean implements MultiItemEntity{


    /**
     * type : 分类
     * content : [{"imgUrl":"123","title":"标题"}]
     */


    public static final int TYPE_CLASSIFY = 1;
    public static final int TYPE_CARD_BAG = 2;

    private String title;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

