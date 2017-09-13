package com.zxcx.shitang.ui.classify;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class ClassifyBean implements MultiItemEntity{


    /**
     * type : 分类
     * content : [{"imgUrl":"123","title":"标题"}]
     */


    public static final int TYPE_CLASSIFY = 1;

    @JSONField(name = "title")
    private String title;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "collectionData")
    private List<ClassifyCardBagBean> dataList;

    @Override
    public int getItemType() {
        return TYPE_CLASSIFY;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ClassifyCardBagBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ClassifyCardBagBean> dataList) {
        this.dataList = dataList;
    }
}

