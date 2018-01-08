package com.zxcx.zhizhe.ui.my.note.noteDetails;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

import java.util.Date;

public class NoteDetailsBean extends RetrofitBaen {

    @SerializedName("id")
    private int id;
    @SerializedName("relatedArticleId")
    private int withCardId;
    @SerializedName("collectionId")
    private int cardBagId;
    @SerializedName("titleImage")
    private String imageUrl;
    @SerializedName("title")
    private String name;
    @SerializedName("modifyTime")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWithCardId() {
        return withCardId;
    }

    public void setWithCardId(int withCardId) {
        this.withCardId = withCardId;
    }

    public int getCardBagId() {
        return cardBagId;
    }

    public void setCardBagId(int cardBagId) {
        this.cardBagId = cardBagId;
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
}

