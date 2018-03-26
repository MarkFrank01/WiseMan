package com.zxcx.zhizhe.ui.card.cardBag;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.Date;

public class CardBagBean extends RetrofitBaen {

    @SerializedName("id")
    private int id;
    @SerializedName("articleType")
    private int cardType;
    @SerializedName("collectingCount")
    private int collectNum;
    @SerializedName("pv")
    private int readNum;
    @SerializedName("titleImage")
    private String imageUrl;
    @SerializedName("title")
    private String name;
    @SerializedName("passTime")
    private Date date;
    @SerializedName("authorName")
    private String author;
    @SerializedName("collectionName")
    private String cardBagName;

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getCardBagName() {
        return cardBagName;
    }

    public void setCardBagName(String cardBagName) {
        this.cardBagName = cardBagName;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

