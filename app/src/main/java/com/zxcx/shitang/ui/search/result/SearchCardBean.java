package com.zxcx.shitang.ui.search.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

public class SearchCardBean extends RetrofitBaen {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "collectingCount")
    private int collectNum;
    @JSONField(name = "likedUsersCount")
    private int likeNum;
    @JSONField(name = "collectionName")
    private String bagName;
    @JSONField(name = "collectionId")
    private int bagId;

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

    public String getBagName() {
        return bagName;
    }

    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    public int getBagId() {
        return bagId;
    }

    public void setBagId(int bagId) {
        this.bagId = bagId;
    }
}

