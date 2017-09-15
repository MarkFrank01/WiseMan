package com.zxcx.shitang.ui.my.collect.collectFolder;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

public class CollectFolderBean extends RetrofitBaen {
    private boolean isChecked;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "titleImage")
    private String imageUrl;
    @JSONField(name = "createTime")
    private Long time;
    @JSONField(name = "collect")
    private int num;

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

