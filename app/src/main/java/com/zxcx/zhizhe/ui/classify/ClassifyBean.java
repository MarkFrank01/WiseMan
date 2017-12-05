package com.zxcx.zhizhe.ui.classify;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.List;

public class ClassifyBean extends RetrofitBaen implements MultiItemEntity{


    /**
     * type : 分类
     * content : [{"imageUrl":"123","title":"标题"}]
     */


    public static final int TYPE_CLASSIFY = 1;

    @JSONField(name = "title")
    private String title;
    @JSONField(name = "collectionData")
    private List<ClassifyCardBagBean> dataList;

    @Override
    public int getItemType() {
        return TYPE_CLASSIFY;
    }

    public String getTitle() {
        return title;
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

