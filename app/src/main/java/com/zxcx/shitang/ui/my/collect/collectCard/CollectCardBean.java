package com.zxcx.shitang.ui.my.collect.collectCard;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

public class CollectCardBean extends RetrofitBaen {
    private boolean isChecked;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "collectingCount")
    private int collectNum;
    @JSONField(name = "likedUsersCount")
    private int likeNum;

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

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}

