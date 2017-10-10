package com.zxcx.zhizhe.ui.search.search;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class SearchBean extends RetrofitBaen {


    /**
     * conent : string
     * createTime : 2017-09-14T02:06:36.225Z
     * id : 0
     */

    @JSONField(name = "conent")
    private String conent;
    @JSONField(name = "createTime")
    private String createTime;
    @JSONField(name = "id")
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

