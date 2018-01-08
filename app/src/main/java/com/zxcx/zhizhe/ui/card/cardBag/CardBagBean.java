package com.zxcx.zhizhe.ui.card.cardBag;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.Date;

public class CardBagBean extends RetrofitBaen {

    @SerializedName("id")
    private int id;
    @SerializedName("titleImage")
    private String imageUrl;
    @SerializedName("title")
    private String name;
    @SerializedName("passTime")
    private Date date;
    @SerializedName("authorName")
    private String author;

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

