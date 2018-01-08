package com.zxcx.zhizhe.ui.my.selectAttention;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class SelectAttentionBean extends RetrofitBaen {
    @SerializedName("interested")
    private boolean isChecked;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String name;
    @SerializedName("titleImage")
    private String imageUrl;

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
}

