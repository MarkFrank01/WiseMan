package com.zxcx.zhizhe.ui.search.search;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class HotSearchBean extends RetrofitBaen {


    /**
     * conent : string
     * createTime : 2017-09-14T02:06:36.225Z
     * id : 0
     */

    @SerializedName("conent")
    private String conent;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("id")
    private int id;

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

