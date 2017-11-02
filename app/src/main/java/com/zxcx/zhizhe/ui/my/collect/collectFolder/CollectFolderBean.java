package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class CollectFolderBean extends RetrofitBaen implements MultiItemEntity{

    private boolean isChecked;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "createTime")
    private Long time;
    @JSONField(name = "articleCount")
    private int num;
    //1为正常数据项，0为添加收藏夹
    private int itemType = 1;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectFolderBean bean = (CollectFolderBean) o;

        return id == bean.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}

