package com.zxcx.shitang.ui.card.card.newCardDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

public class CardDetailsBean extends RetrofitBaen {

    @JSONField(name = "like")
    private boolean isLike;
    @JSONField(name = "collect")
    private boolean isCollect;
    @JSONField(name = "likedUsersCount")
    private int likeNum;
    @JSONField(name = "collectingCount")
    private int collectNum;

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
}

