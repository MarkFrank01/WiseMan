package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class CardDetailsBean extends RetrofitBaen {

    @JSONField(name = "like")
    private boolean isLike;
    @JSONField(name = "disagree")
    private boolean isUnLike;
    @JSONField(name = "collect")
    private boolean isCollect;
    @JSONField(name = "likedUsersCount")
    private int likeNum;
    @JSONField(name = "disagreeUsersCount")
    private int unLikeNum;
    @JSONField(name = "collectingCount")
    private int collectNum;
    @JSONField(name = "collectionId")
    private int cardBagId;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "collectionName")
    private String cardBagName;

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
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

    public boolean isUnLike() {
        return isUnLike;
    }

    public void setUnLike(boolean unLike) {
        isUnLike = unLike;
    }

    public int getUnLikeNum() {
        return unLikeNum;
    }

    public void setUnLikeNum(int unLikeNum) {
        this.unLikeNum = unLikeNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardBagId() {
        return cardBagId;
    }

    public void setCardBagId(int cardBagId) {
        this.cardBagId = cardBagId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardBagName() {
        return cardBagName;
    }

    public void setCardBagName(String cardBagName) {
        this.cardBagName = cardBagName;
    }
}

