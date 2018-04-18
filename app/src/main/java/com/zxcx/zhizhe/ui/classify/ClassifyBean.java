package com.zxcx.zhizhe.ui.classify;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.List;

public class ClassifyBean extends RetrofitBaen implements MultiItemEntity{


    /**
     * type : 分类
     * content : [{"imageUrl":"123","title":"标题"}]
     */


    public static final int TYPE_CLASSIFY = 1;

    @SerializedName("title")
    private String title;
    @SerializedName("collectionData")
    private List<ClassifyCardBean> dataList;

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

    public List<ClassifyCardBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ClassifyCardBean> dataList) {
        this.dataList = dataList;
    }
}

