package com.zxcx.zhizhe.ui.my.creation.creationDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.Date;

public class RejectDetailsBean extends RetrofitBaen {

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "collectionId")
    private int cardBagId;
    @JSONField(name = "collectionName")
    private String cardBagName;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "authorName")
    private String authorName;
    @JSONField(name = "unpassReason")
    private String rejectReason;
    @JSONField(name = "modifyTime")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardBagId() {
        return cardBagId;
    }

    public void setCardBagId(int cardBagId) {
        this.cardBagId = cardBagId;
    }

    public String getCardBagName() {
        return cardBagName;
    }

    public void setCardBagName(String cardBagName) {
        this.cardBagName = cardBagName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

